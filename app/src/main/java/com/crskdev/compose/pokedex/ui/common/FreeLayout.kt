package com.crskdev.compose.pokedex.ui.common

import androidx.compose.Composable
import androidx.ui.core.*
import androidx.ui.layout.LayoutSize
import androidx.ui.unit.*

/**
 * Created by Cristian Pela on 01.02.2020.
 */
@Composable
fun FreeLayout(modifier: Modifier = LayoutSize.Fill, children: @Composable() FreeChildScope.() -> Unit) {
    val density = ambientDensity()
    val freeChildren: @Composable() () -> Unit = { FreeChildScope(density).children() }
    Layout(modifier = modifier, children = freeChildren) { measurables, constraints ->
        val childConstraints = constraints.copy(minWidth = 0.ipx, minHeight = 0.ipx)
        val placeables = measurables.map {
            it.measure(childConstraints)
        }
        println("Placeables " + placeables.map { it.height })
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val freePosition = measurables[index].freeChildData
                placeable.place(
                    freePosition(
                        IntPxSize(
                            constraints.maxWidth - placeable.width,
                            constraints.maxHeight - placeable.height
                        )
                    )
                )
            }
        }
    }
}

private val Measurable.freeChildData: FreePosition
    get() = (parentData as? FreePosition) ?: { IntPxPosition.Origin }

class FreeChildScope(private val density: Density) {

    fun FreePosition(x: Dp = Dp.Hairline, y: Dp = Dp.Hairline, alignment: Alignment = Alignment.TopLeft): PositionModifier =
        PositionModifier {
            alignment.align(it) + withDensity(density) {
                IntPxPosition(x.toIntPx(), y.toIntPx())
            }
        }

    inner class PositionModifier internal constructor(private val position: FreePosition) :
        ParentDataModifier {
        override fun DensityScope.modifyParentData(parentData: Any?): FreePosition {
            return (parentData as? FreePosition) ?: position
        }
    }
}

typealias FreePosition = (IntPxSize) -> IntPxPosition