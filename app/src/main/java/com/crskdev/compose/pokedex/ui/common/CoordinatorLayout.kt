package com.crskdev.compose.pokedex.ui.common

import androidx.annotation.FloatRange
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.remember
import androidx.ui.core.*
import androidx.ui.foundation.ColoredRect
import androidx.ui.foundation.ValueHolder
import androidx.ui.foundation.animation.FlingConfig
import androidx.ui.foundation.animation.animatedDragValue
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.Draggable
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.SolidColor
import androidx.ui.layout.*
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import androidx.ui.unit.*
import kotlin.math.roundToInt

/**
 * Created by Cristian Pela on 27.01.2020.
 */
@Composable
fun CoordinatorLayout(modifier: Modifier = LayoutSize.Fill, collapsingAmount: CollapsingAmount = CollapsingAmount()) {

    val position = animatedDragValue(initial = 0f, minBound = 0f, maxBound = 0f)

    val step = remember { StepHolder() }

    val flingConfig = FlingConfig()

    Container(modifier = modifier, alignment = Alignment.TopLeft) {
        Draggable(
            dragDirection = DragDirection.Vertical,
            dragValue = position,
            onDragStopped = { position.fling(flingConfig, it) },
            onDragValueChangeRequested = { position.animatedFloat.snapTo(it) },
            enabled = true
        ) {
            val children = @Composable() {
                ColoredRect(
                    modifier = LayoutTag("scrim") + LayoutWidth.Fill + LayoutHeight(56.dp * 3),
                    brush = SolidColor(Color.Red)
                ) //scrim
                Column(LayoutTag("scroller")) {
                    repeat(20) {
                        Surface(LayoutWidth.Fill + LayoutHeight(56.dp), color = Color.Gray) {
                            Text(text = "#$it", style = TextStyle(color = Color.White))
                        }
                        Spacer(modifier = LayoutHeight(4.dp))
                    }
                }
                //scroll pane
                ColoredRect(
                    modifier = LayoutTag("toolbar") + LayoutWidth.Fill + LayoutHeight(56.dp),
                    brush = SolidColor(Color.Red)
                ) // toolbar
            }
            Clip(shape = RectangleShape) {
                Layout(children = children) { measurables, constraints ->
                    val placeables = measurables.map {
                        val childConstraints = if (it.tag == "scroller") {
                            constraints.copy(maxHeight = IntPx.Infinity)
                        } else {
                            constraints.copy(minWidth = IntPx.Zero, minHeight = IntPx.Zero)
                        }
                        it.measure(childConstraints)
                    }
                    val (scrim, scroller, toolbar) = placeables
                    val height: IntPx =
                        min(scroller.height +  scrim.height + toolbar.height, constraints.maxHeight)
                    layout(constraints.maxWidth, height) {
                        val positionPx = position.value.roundToInt().ipx
                        when (step.value) {
                            Step.INIT -> {
                                position.setBounds(
                                    -(height.value.toFloat() - toolbar.height.value),
                                    toolbar.height.value.toFloat()
                                )
                                scrim.place(IntPx.Zero, toolbar.height)
                                scroller.place(IntPx.Zero, toolbar.height + scrim.height)
                                step.setter = Step.SCROLLER
                                collapsingAmount.setter = 1f
                                collapsingAmount.scrimSize = toolbar.width.toDp() to (toolbar.height + scrim.height).toDp()
                                position.animatedFloat.snapTo(toolbar.height.value.toFloat())
                            }
                            Step.SCROLLER -> {
                                scrim.place(IntPx.Zero, positionPx)
                                scroller.place(IntPx.Zero, positionPx + scrim.height)
                                collapsingAmount.setter = ((positionPx.value.toFloat() + scrim.height.value.toFloat())
                                        / (toolbar.height.value.toFloat() + scrim.height.value.toFloat()))
                                            .coerceIn(0f, 1f)
                            }
                        }
                        toolbar.place(IntPx.Zero, IntPx.Zero)
                    }
                }
            }
        }
    }
}

@Model
class CollapsingAmount : ValueHolder<Float> {
    @FloatRange(from = 0.0, to = 1.0)
    internal var setter: Float = 1.0f
    var scrimSize: Pair<Dp, Dp> = Dp.Hairline to Dp.Hairline
        internal set
    override val value: Float get() = setter
}

enum class Step {
    INIT, SCRIM, SCROLLER
}

class StepHolder(var setter: Step = Step.INIT) : ValueHolder<Step> {
    override val value: Step get() = setter
}

class FloatValueHolder(var setter: Float = 0f) : ValueHolder<Float> {
    override val value: Float
        get() = setter
}
