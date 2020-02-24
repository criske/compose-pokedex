package com.crskdev.compose.pokedex.ui.common

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Container
import androidx.ui.layout.Padding
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import androidx.ui.unit.sp

/**
 * Created by Cristian Pela on 29.01.2020.
 */
@Composable
fun Chip(text: String, textColor: Color, bgColor: Color, modifier: Modifier = Modifier.None) {
    Container(modifier = modifier) {
        DrawShape(shape = RoundedCornerShape(38.dp), color = bgColor)
        Padding(top = 4.dp, bottom = 4.dp, left = 8.dp, right = 8.dp) {
            Text(text = text, style = TextStyle(color = textColor, fontSize = 8.sp))
        }
    }
}

@Composable
fun PokemonTypeChip(type: String) {
    Chip(text = type, textColor = Color.White, bgColor = Color.White.copy(alpha = 0.25f) )
}