@file:Suppress("MoveLambdaOutsideParentheses")

package com.crskdev.compose.pokedex.ui.utils

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.lightColorPalette
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.ui.common.WindowInsets
import com.crskdev.compose.pokedex.ui.common.WindowInsetsAmbient

/**
 * Created by Cristian Pela on 22.01.2020.
 */
@Composable
fun Bootstrap(ambients: Ambients = emptyList(),
              theme: Theme = lightColorPalette() to androidx.ui.material.Typography(),
              children: @Composable() () -> Unit) {
    val allAmbients: Ambients = remember {
        val all: MutableList<AmbientProvider<*>> = mutableListOf()
        all.add({ WindowInsetsAmbient.Provider(value = WindowInsets(24.dp, 48.dp), children = it) })
        ambients.forEach {
            all.add(it)
        }
        all.toList()
    }
    MaterialTheme(colors = theme.first, typography = theme.second) {
        AmbientsProvider(ambients = allAmbients, content = children)
    }
}

@Composable
private fun AmbientsProvider(ambients: Ambients, content: @Composable() () -> Unit) {
    ambients.fold(content, { current, ambient ->
        { ambient(current) }
    }).invoke()
}

typealias Ambients = List<AmbientProvider<*>>

typealias  AmbientProvider<T> = @Composable() (@Composable() () -> Unit) -> Unit

typealias Theme = Pair<ColorPalette, androidx.ui.material.Typography>