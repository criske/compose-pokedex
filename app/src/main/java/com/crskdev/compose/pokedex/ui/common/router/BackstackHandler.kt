package com.crskdev.compose.pokedex.ui.common.router

import android.os.Bundle
import androidx.compose.Ambient

/**
 * Created by Cristian Pela on 20.01.2020.
 */
class BackstackHandler(
    private val serializer: ScreenSerializer = DummySerializer(),
    private val onFinish: OnFinish) {

    private val backstack: Backstack = Backstack()

    private val listeners = mutableMapOf<String, OnBackstackChangeListener>()

    enum class BackPressedEventSource { SYSTEM, USER }

    sealed class BackstackEventType {
        object PUSH : BackstackEventType()
        class POP(val data: Any = Any()) : BackstackEventType()
        object TOP : BackstackEventType()
    }

    class Backstack : Iterable<Any> {

        data class Entry(val screen: Any, var childBackstack: Backstack? = null)

        var name: String? = null

        private var backstack: List<Entry> = emptyList()

        fun push(entry: Entry) {
            backstack += entry
        }

        fun pop(): Entry? {
            backstack = backstack.subList(0, backstack.lastIndex)
            return top()
        }

        fun isEmpty() = backstack.isEmpty()

        fun top(): Entry? = backstack.lastOrNull()

        override fun iterator(): Iterator<Any> = backstack.iterator()

    }

    class Subscription internal constructor(
        private val routerName: String,
        private val listeners: MutableMap<String, OnBackstackChangeListener>) {
        fun clear() {
            listeners.remove(routerName)
        }
    }

    fun push(routerName: String, screen: Any) {
        var currentBackstack: Backstack? = backstack
        while (true) {
            requireNotNull(currentBackstack?.name)
            if (currentBackstack?.name == routerName) {
                currentBackstack.push(Backstack.Entry(screen))
                notifyListeners(routerName, BackstackEventType.PUSH, screen)
                break
            }
            currentBackstack = currentBackstack?.top()?.childBackstack
                ?: throw Exception("Backstack with name $routerName not found")
        }

    }

    fun pop(source: BackPressedEventSource, routerName: String? = null, dataResult: Any = Any()): Boolean {
        val rooBackstack = backstack
        var currentBackstack: Backstack? = backstack
        var popped = false
        while (true) {
            if (currentBackstack == null || currentBackstack.isEmpty())
                break
            requireNotNull(currentBackstack.name)
            val top = currentBackstack.top()
            if (currentBackstack.name == routerName || top?.childBackstack == null) {
                val entry = currentBackstack.pop().apply {
                    notifyListeners(currentBackstack?.name!!, BackstackEventType.POP(dataResult), this?.screen)
                }
                popped = entry != null
                break
            }
            currentBackstack = currentBackstack.top()?.childBackstack
        }
        if (rooBackstack.isEmpty() && source == BackPressedEventSource.USER) {
            onFinish()
        }
        return popped
    }

    fun onBackPressed(): Boolean = pop(BackPressedEventSource.SYSTEM)

    fun <S> attachRouter(routerName: String, defaultScreen: S, listener: OnBackstackChangeListener): Subscription {

        require(!listeners.keys.contains(routerName)) {
            "A router with the name $routerName is already attached. Please choose a unique name"
        }

        //append router's name to the the backstack or child backstack
        if (backstack.name == null) {
            backstack.name = routerName
        } else {
            var top: Backstack.Entry? = backstack.top()
            while (true) {
                val childBackstack: Backstack? = top?.childBackstack
                if (childBackstack == null) {
                    top?.childBackstack = Backstack().apply { name = routerName }
                    break
                }
                top = childBackstack.top()
            }
        }

        //register listener
        listeners[routerName] = listener

        //send the top screen to router
        val top = top(routerName)
        if (top != null) {
            notifyListeners(routerName, BackstackEventType.TOP, top)
        } else {
            push(routerName, defaultScreen as Any)
        }

        return Subscription(routerName, listeners)
    }

    fun top(routerName: String? = null): Any? {
        var currentBackstack: Backstack? = backstack
        while (true) {
            requireNotNull(currentBackstack?.name)
            val top = currentBackstack?.top()
            if (currentBackstack?.name == routerName || top?.childBackstack == null) {
                return top
            }
            currentBackstack = top.childBackstack
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        serializer.onSaveInstanceState(backstack, outState)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        serializer.onRestoreInstanceState(savedInstanceState)
    }

    private fun notifyListeners(routerName: String, eventType: BackstackEventType, screen: Any?) {
        listeners[routerName]?.invoke(eventType, screen)
    }
}

typealias OnBackstackChangeListener = (BackstackHandler.BackstackEventType, Any?) -> Unit

typealias OnFinish = () -> Unit

val BackstackHandlerAmbient = Ambient.of<BackstackHandler>()

class DummySerializer : ScreenSerializer {

    override fun onSaveInstanceState(backstack: BackstackHandler.Backstack, savedInstanceState: Bundle) {
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle): BackstackHandler.Backstack {
        return BackstackHandler.Backstack()
    }

}