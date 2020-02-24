package com.crskdev.compose.pokedex.ui.home

import androidx.compose.Composable
import androidx.compose.ambient
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import com.crskdev.compose.pokedex.ui.Screen
import com.crskdev.compose.pokedex.ui.common.ScreenDevices
import com.crskdev.compose.pokedex.ui.common.router.NavigatorAmbient

/**
 * Created by Cristian Pela on 02.01.2020.
 */
@Composable
fun HomeScreen() {
    val navigator = ambient(NavigatorAmbient)
    HomeScreen {
        when (it) {
            HomeAction.Category.Pokedex -> navigator.push(Screen.Pokedex)
            HomeAction.Category.Moves -> {
            }
            HomeAction.Category.Abilities -> {
            }
            HomeAction.Category.Items -> {
            }
            HomeAction.Category.Locations -> {
            }
            HomeAction.Category.TypeCharts -> {
            }
            is HomeAction.SearchItem -> {
            }
        }
    }
}

@Composable
private fun HomeScreen(action: (HomeAction) -> Unit) {
    VerticalScroller {
        Column {
            Category(action)
            News()
        }
    }
}
