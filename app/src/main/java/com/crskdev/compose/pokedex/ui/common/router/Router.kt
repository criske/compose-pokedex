package com.crskdev.compose.pokedex.ui.common.router

import androidx.compose.*
import androidx.ui.animation.Crossfade

/**
 * Created by Cristian Pela on 20.01.2020.
 */

@Suppress("UNCHECKED_CAST")
@Composable
fun <S> Router(name: String, defaultScreen: S, children: @Composable() (S) -> Unit) {

    val backstackHandler = ambient(BackstackHandlerAmbient)

    val screen = state<S?> { null }

    val navigator = remember(name) { Navigator(name, backstackHandler) }

    onActive {
        val subscription = backstackHandler.attachRouter(name, defaultScreen) { _, s ->
            if (s != null)
                screen.value = s as S
        }
        onDispose { subscription.clear() }
    }

    if (screen.value != null) {
        NavigatorAmbient.Provider(value = navigator) {
            Crossfade(current = screen.value!!, children = children)
        }
    }

}

class Navigator(private val routerName: String,
                private val backstackHandler: BackstackHandler) {

    fun <S> push(screen: S, routerName: String? = null) {
        backstackHandler.push(routerName ?: this.routerName, screen as Any)
    }

    fun pop(routerName: String? = null, dataResult: Any = Any()) {
        backstackHandler.pop(
            BackstackHandler.BackPressedEventSource.USER,
            routerName ?: this.routerName,
            dataResult
        )
    }

}

val NavigatorAmbient = Ambient.of<Navigator>()