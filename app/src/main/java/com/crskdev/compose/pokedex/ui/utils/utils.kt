package com.crskdev.compose.pokedex.ui.utils

import androidx.annotation.ColorRes
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.remember
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.ui.core.ContextAmbient
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb

/**
 * Created by Cristian Pela on 03.01.2020.
 */
@Composable
fun nativeColor(@ColorRes color: Int): Color {
    val context = ambient(ContextAmbient)
    return remember(color) { Color(ContextCompat.getColor(context, color)) }
}

fun String.capitalizeEach(): String =
    split(" ").joinToString(" ") { it.capitalize() }