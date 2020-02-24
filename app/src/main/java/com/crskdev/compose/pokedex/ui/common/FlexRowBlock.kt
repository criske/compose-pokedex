package com.crskdev.compose.pokedex.ui.common

import androidx.compose.Composable
import androidx.ui.core.*
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Container
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.*

/**
 * Created by Cristian Pela on 25.01.2020.
 */
@Composable
fun FlexRowBlock(modifier: Modifier = Modifier.None, spacing: Dp = 0.dp, children: @Composable() () -> Unit) {
        Layout(modifier = modifier, children = children) { measurables, constraints ->
            val delegate = FlowBlockMeasurer(measurables, constraints, spacing, density)
            layout(delegate.maxWidth, delegate.maxHeight) {
                delegate.forEach {
                    it.placeable.place(it.x, it.y)
                }
            }
        }
}

private class FlowBlockMeasurer(measurables: List<Measurable>,
                                private val constraints: Constraints,
                                private val spacing: Dp,
                                density: Density) :
    Iterable<FlowBlockMeasurer.PlaceablePosition> {

    private val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0.ipx, minHeight = 0.ipx)) }

    private val positions = mutableListOf<PlaceablePosition>()

    var maxHeight: IntPx = 0.ipx
        private set

    var maxWidth: IntPx = 0.ipx
        private set

    init {
        withDensity(density) {
            var maxRowHeight = 0.ipx
            var xOffset = 0.ipx
            var yOffset = 0.ipx
            var rowCount = 0
            var isCurrentRowHeightChanged = false
            for (curr in placeables.indices) {
                val next = curr + 1
                positions.add(PlaceablePosition(xOffset, yOffset, placeables[curr]))
                if (placeables[curr].height > maxRowHeight) {
                    maxRowHeight = placeables[curr].height
                    maxHeight = maxRowHeight
                    isCurrentRowHeightChanged = true
                }
                xOffset = placeables[curr].width  + spacing.toIntPx()
                if (xOffset > maxWidth) {
                    maxWidth = xOffset
                }
                if (next <= placeables.lastIndex && xOffset  + placeables[next].width > constraints.maxWidth) {
                    xOffset = 0.ipx
                    yOffset = maxRowHeight  + spacing.toIntPx()
                    maxRowHeight = 0.ipx
                    isCurrentRowHeightChanged = false
                    rowCount
                }
            }
            maxHeight = (spacing.toIntPx() * rowCount + (if (isCurrentRowHeightChanged) placeables.lastOrNull()?.height
                ?: 0.ipx else 0.ipx))
        }
    }

    override fun iterator(): Iterator<PlaceablePosition> = positions.iterator()

    class PlaceablePosition(val x: IntPx, val y: IntPx, val placeable: Placeable)

}

@Preview
@Composable
fun SimpleFlowRowPreview() {
    ScreenDevices.HuaweiP10 {
        Container(width = 100.dp, alignment = Alignment.TopLeft) {
            FlexRowBlock {
                SimpleRect()
                SimpleRect(color = Color.Green)
                SimpleRect(color = Color.Yellow)
                SimpleRect(color = Color.Blue)
                SimpleRect(color = Color.Gray)
                SimpleRect(color = Color.Cyan)
            }
        }
    }
}

@Composable
fun SimpleRect(width: Dp = 100.dp, height: Dp = 100.dp, color: Color = Color.Red) {
    Container(width = width, height = height) {
        DrawShape(shape = RectangleShape, color = color)
    }
}