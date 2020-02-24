package com.crskdev.compose.pokedex.ui.pokedex

import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.core.Layout
import androidx.ui.core.Modifier
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutSize
import androidx.ui.layout.Stack
import androidx.ui.layout.StackScope
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.unit.IntPxPosition
import androidx.ui.unit.dp
import androidx.ui.unit.ipx
import com.crskdev.compose.pokedex.ui.common.localThemeType
import com.crskdev.compose.pokedex.ui.common.onSurface
import com.crskdev.compose.pokedex.ui.utils.DrawImage

/**
 * Created by Cristian Pela on 23.01.2020.
 */
@Composable
fun PokedexItemContainer(
    pokeballCache: Image,
    modifier: Modifier = Modifier.None,
    color: Color = (MaterialTheme.colors()).surface,
    children: @Composable() StackScope.() -> Unit) {
    val ownModifier = remember(modifier) { modifier }
    val pokeballTint = color.localThemeType.onSurface().copy(alpha = 0.05f)
    Surface(
        color = color,
        shape = RoundedCornerShape(15.dp),
        elevation = 3.dp
    ) {
        Container(width = 150.dp, height = 110.dp) {
            val allChildren = @Composable() {
                DrawImage(pokeballCache, size = 80.dp, tint = pokeballTint)
                Stack(modifier = LayoutSize.Fill) {
                    this.children()
                }
            }
            Layout(modifier = ownModifier, children = allChildren) { measurables, constraints ->
                val placeables = measurables.map {
                    it.measure(
                        constraints.copy(
                            minWidth = 0.ipx,
                            minHeight = 0.ipx
                        )
                    )
                }
                layout(constraints.maxWidth, constraints.maxHeight) {
                    val pokeballPlaceable = placeables.first()
                    pokeballPlaceable.place(
                        Alignment.BottomRight
                            .align(pokeballPlaceable.size)
                            .minus(IntPxPosition(5.dp.toIntPx(), 5.dp.toIntPx()))
                    )
                    placeables.last().place(0.ipx, 0.ipx)
                }
            }
        }
    }
}
