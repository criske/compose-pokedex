package com.crskdev.compose.pokedex.ui.utils

import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.*
import androidx.core.view.postDelayed
import androidx.ui.core.ContextAmbient
import androidx.ui.core.setContent
import com.crskdev.compose.pokedex.ui.App
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandler
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandlerAmbient


/**
 * Created by Cristian Pela on 16.01.2020.
 */
@Composable
fun Window(onCloseRequest: () -> Unit, children: @Composable() (WindowCloseHandler) -> Unit) {
    val context = ambient(ContextAmbient)
    Recompose { recompose ->
        val dialog = remember {
            DialogWrapper(context, recompose, onCloseRequest)
        }
        val windowCloseHandler: () -> Unit = remember {
            {
                dialog.dismiss()
                dialog.disposeComposition()
                onCloseRequest()
            }
        }
        onActive {
            dialog.show()

            onDispose {
                dialog.dismiss()
                dialog.disposeComposition()
            }
        }
        onCommit {
            val content =@Composable() {
                children(windowCloseHandler)
            }
            dialog.setContent(content)
        }
    }
}

typealias WindowCloseHandler = () -> Unit

private class DialogWrapper(context: Context,
                            recompose: () -> Unit,
                            val onCloseRequest: () -> Unit) : Dialog(context) {

    private val frameLayout = FrameLayout(context)

    private val backstackHandler = BackstackHandler{
        onCloseRequest()
    }

    init {
        frameLayout.viewTreeObserver.addOnGlobalLayoutListener{
            recompose()
        }
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.addFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(frameLayout)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    }

    fun setContent(children: @Composable() () -> Unit) {
        val content: @Composable() ()-> Unit = {
            Bootstrap(ambients = listOf(
                { c -> BackstackHandlerAmbient.Provider(backstackHandler, c) }
            )) {
                children()
            }
        }
        frameLayout.setContent(content)
    }

    fun disposeComposition() {
        frameLayout.disposeComposition()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        if (result) {
            onCloseRequest()
        }
        return result
    }

    override fun cancel() {
        // Prevents the dialog from dismissing itself
        return
    }

    override fun onBackPressed() {
        if(!backstackHandler.onBackPressed())
            onCloseRequest()
    }
}