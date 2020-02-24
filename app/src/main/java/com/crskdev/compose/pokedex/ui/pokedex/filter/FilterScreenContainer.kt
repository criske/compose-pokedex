package com.crskdev.compose.pokedex.ui.pokedex.filter

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp

/**
 * Created by Cristian Pela on 18.01.2020.
 */

@Composable
fun FilterScreenContainer(
    modifier: Modifier = Modifier.None,
    children: @Composable() () -> Unit) {
    Surface(modifier = modifier, shape = RoundedCornerShape(topLeft = 30.dp, topRight = 30.dp)) {
        children()
    }
}