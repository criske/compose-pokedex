package com.crskdev.compose.pokedex.ui

import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.ColoredRect
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.constraintlayout.ConstraintLayout
import androidx.ui.layout.constraintlayout.ConstraintSet
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.lerp
import androidx.ui.unit.sp
import com.crskdev.compose.pokedex.ui.common.CollapsingAmount
import com.crskdev.compose.pokedex.ui.common.CoordinatorLayout
import com.crskdev.compose.pokedex.ui.common.FreeLayout
import com.crskdev.compose.pokedex.ui.common.WithWindowInsets
import com.crskdev.compose.pokedex.ui.common.router.Router
import com.crskdev.compose.pokedex.ui.dashboard.DashboardScreen
import com.crskdev.compose.pokedex.ui.home.HomeScreen
import com.crskdev.compose.pokedex.ui.pokedex.PokedexScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/**
 * Created by Cristian Pela on 02.01.2020.
 */
@Composable
fun App() {
    Surface {
        Router<Screen>(name = "app", defaultScreen = Screen.Home) {
            when (it) {
                Screen.Home -> HomeScreen()
                Screen.Pokedex -> PokedexScreen()
                is Screen.About -> DashboardScreen(id = it.id)
            }
        }

    }
//    val evolution = remember {
//        EvolutionTab.Data.EvolutionChain(
//            EvolutionTab.Data.EvolutionChain.Pokemon(
//                1,
//                "Bulbasaur",
//                ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            EvolutionTab.Data.EvolutionChain.Pokemon(
//                2,
//                "Ivysaur",
//                ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            "Lvl 16"
//        )
//    }
//    WithWindowInsets {
//        EvolutionTab.EvolutionChainItem(evolution = evolution)
//    }


//    WithWindowInsets {
//        val amount = remember { CollapsingAmount() }
//        FreeLayout(modifier = LayoutSize.Fill) {
//            CoordinatorLayout(collapsingAmount = amount)
//            FreeLayout(
//                modifier = FreePosition(alignment = Alignment.TopLeft) + LayoutSize(
//                    amount.scrimSize.first,
//                    amount.scrimSize.second
//                )
//            ) {
//                Text(
//                    modifier = FreePosition(
//                        x = lerp(20.dp, 64.dp, 1 - amount.value),
//                        y = lerp(amount.scrimSize.second - 64.dp, 16.dp, 1 - amount.value)
//                    ),
//                    text = "Title",
//                    style = TextStyle(
//                        color = Color.White,
//                        fontSize = lerp(36.sp, 24.sp, 1 - amount.value),
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//
//            }
//        }
//
//        println("Collapsing amount ${amount.value}")
//    }

}


@Composable
fun Counter() {
    var count by state { 0 }
    Text("Count $count")
    launch {
        repeat(100) {
            delay(1000)
            count++
        }
    }
}

@Composable
fun launch(context: CoroutineContext = ambient(CoroutineContextAmbient),
           block: suspend CoroutineScope.() -> Unit) {
    val job = remember { Job() }
    onDispose {
        job.cancel()
    }
    return remember {
        CoroutineScope(job).launch(context = context, block = block)
    }
}


sealed class Screen {
    object Home : Screen()
    object Pokedex : Screen()
    class About(val id: Int) : Screen()
}

@Composable
fun ApplicationFillFailed() {
    MaterialTheme {
        ConstraintLayout(constraintSet = ConstraintSet {
            val a = tag("a").apply {
                constrainHorizontallyTo(parent)
                top constrainTo parent.top
            }
            val b = tag("b").apply {
                constrainHorizontallyTo(parent)
                top constrainTo a.bottom
                top.margin = 16.dp
            }
        }) {
            ColoredRect(modifier = LayoutTag("a") + LayoutWidth.Fill + LayoutHeight(100.dp))
            ColoredRect(modifier = LayoutTag("a") + LayoutSize.Fill)
        }
    }
}

@Composable
fun ApplicationMultiMeasured() {
    MaterialTheme {
        ConstraintLayout(constraintSet = ConstraintSet {
            with(tag("a")) {
                constrainHorizontallyTo(parent)
                top constrainTo parent.top
                center()
            }
        }) {
            Column(modifier = LayoutTag("a") + LayoutSize(400.dp)) {
                var selected by state { 0 }
                TabRow(items = listOf("A", "B"), selectedIndex = selected) { index, item ->
                    Tab(text = item, selected = index == selected) {
                        selected = index
                    }
                }
                Container {
                    when (selected) {
                        0 -> ColoredRect(modifier = LayoutSize.Fill, color = Color.Green)
                        1 -> ColoredRect(modifier = LayoutSize.Fill, color = Color.Yellow)
                    }
                }
            }
        }
    }
}


@Composable
fun ColoredRect(modifier: Modifier, color: Color = Color.Red) {
    Container(modifier) {
        DrawShape(shape = RectangleShape, color = color)
    }
}




