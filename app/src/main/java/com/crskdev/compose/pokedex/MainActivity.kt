package com.crskdev.compose.pokedex

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.crskdev.compose.pokedex.ui.App
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandler
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandlerAmbient
import com.crskdev.compose.pokedex.ui.common.router.ScreenSerializer
import com.crskdev.compose.pokedex.ui.utils.Bootstrap
import org.json.JSONArray
import org.json.JSONObject

@Suppress("MoveLambdaOutsideParentheses")
class MainActivity : AppCompatActivity() {

    private val backstackHandler = BackstackHandler(ScreenSerializerImpl()) {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            Bootstrap(ambients = listOf(
                { c -> BackstackHandlerAmbient.Provider(backstackHandler, c) }
            )) {
                App()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        backstackHandler.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        backstackHandler.onRestoreInstanceState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (!backstackHandler.onBackPressed()) {
            super.onBackPressed()
        }
    }
}

class ScreenSerializerImpl : ScreenSerializer {

    companion object {
        private const val BACKSTACK_KEY = "BACKSTACK_KEY"
    }

    override fun onSaveInstanceState(backstack: BackstackHandler.Backstack, savedInstanceState: Bundle) {
        var backstackJSON = JSONArray()
        backstack.forEach {
            val screen = it as BackstackHandler.Backstack.Entry
            val jsonObject = JSONObject().apply {
                put("name", screen.screen)
            }

        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle): BackstackHandler.Backstack {
        return BackstackHandler.Backstack()
    }

}
