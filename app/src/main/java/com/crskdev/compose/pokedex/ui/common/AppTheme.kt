package com.crskdev.compose.pokedex.ui.common

import androidx.compose.Composable
import androidx.core.graphics.ColorUtils
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb
import androidx.ui.material.MaterialTheme

/**
 * Created by Cristian Pela on 03.02.2020.
 */
enum class LocalThemeType {
    LIGHT, DARK
}

val Color.localThemeType
    get() = ColorUtils.calculateLuminance(toArgb()).let {
        val ratio = it
        if (ratio > 0.6f) {
            LocalThemeType.LIGHT
        } else {
            LocalThemeType.DARK
        }
    }

@Composable
fun LocalThemeType.onSurface(onSurface: Color? = null, surface: Color? = null): Color {
    val colors = MaterialTheme.colors()
    return if (this == LocalThemeType.LIGHT) {
        onSurface ?: colors.onSurface
    } else {
        surface ?: colors.surface
    }
}
