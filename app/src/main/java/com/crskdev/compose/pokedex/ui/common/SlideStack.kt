@file:Suppress("UNCHECKED_CAST")

package com.crskdev.compose.pokedex.ui.common

import androidx.animation.AnimatedFloat
import androidx.animation.AnimationBuilder
import androidx.animation.AnimationEndReason
import androidx.animation.TweenBuilder
import androidx.compose.Composable
import androidx.compose.key
import androidx.compose.remember
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutSize
import androidx.ui.unit.*

/**
 * Created by Cristian Pela on 15.01.2020.
 */
@Composable
fun <S> SlideStack(
    modifier: Modifier = LayoutSize.Fill,
    controlAreaHeader: Dp = 100.dp,
    controlArea: @Composable() (SlideController<S>) -> Unit,
    children: SlideStackChildren<S>.(ScreenState.SnapShot<S?>) -> Unit) {


    val state = remember { ScreenState<S?>() }
    val slideFloat = animatedFloat(0f)
    val slideController = remember { SlideController(state, slideFloat) }

    val slideScreenChildren: @Composable() () -> Unit =
        with(SlideStackChildren(state)) {
            this.children(state.snapshot())
            val composable = @Composable() {
                slideScreenChildren.forEach {
                    it()
                }
            }
            composable
        }

    val allChildren: @Composable() () -> Unit = @Composable() {
        controlArea(slideController)
        slideScreenChildren()
    }


    Clip(shape = RectangleShape) {
        Container(alignment = Alignment.TopLeft) {
            RepaintBoundary {
                Layout(
                    modifier = modifier,
                    children = allChildren
                ) { measurables, constraints ->
                    val placeables = mutableListOf<Placeable>()
                    measurables.indices.forEach { i ->
                        val measurable = measurables[i]
                        placeables.add(
                            measurable.measure(
                                constraints.copy(
                                    minHeight = 0.ipx,
                                    minWidth = 0.ipx
                                )
                            )
                        )
                    }
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        val controlAreaPlaceable = placeables.first()!!
                        val screenPlaceable =
                            placeables.filterIndexed { i, _ -> state.value?.equals(measurables[i].parentData as S) == true }
                                .firstOrNull()
                        controlAreaPlaceable.place(
                            0.px,
                            0.px - (slideFloat.value * (screenPlaceable?.height?.toPx() ?: 0.px))
                                .coerceAtMost(controlAreaHeader.toPx())
                        )
                        screenPlaceable?.place(
                            0.px, controlAreaPlaceable.height.toPx() - slideFloat.value *
                                    min(
                                        screenPlaceable.height.toPx(),
                                        controlAreaPlaceable.height.toPx() - controlAreaHeader.toPx()
                                    )
                        )
                    }
                }
            }
        }

    }
}

class ScreenState<S>(var value: S? = null, var status: Status = Status.COLLAPSED) {
    enum class Status {
        EXPANDED, COLLAPSED, MOVING
    }

    data class SnapShot<S>(val value: S?, val status: Status = Status.COLLAPSED)

    fun snapshot() = SnapShot(value, status)
}

class SlideController<S>(
    private val state: ScreenState<S?>,
    private val animatedFloat: AnimatedFloat
) {

    fun state(): ScreenState.SnapShot<S?> = state.snapshot()

    fun changeScreen(screen: S, anim: AnimationBuilder<Float> = TweenBuilder()) {
        state.status = ScreenState.Status.MOVING
        if (state.value != null && state.value != screen) {
            animatedFloat.animateTo(0f, anim = anim, onEnd = { reason, _ ->
                if (reason == AnimationEndReason.TargetReached) {
                    state.value = screen
                    animatedFloat.animateTo(1f, anim = anim, onEnd = { _, _ ->
                        state.status = ScreenState.Status.EXPANDED
                    })
                }
            })
        } else {
            state.value = screen
            animatedFloat.animateTo(1f, anim = anim, onEnd = { _, _ ->
                state.status = ScreenState.Status.EXPANDED
            })
        }
    }

    fun collapse(anim: AnimationBuilder<Float> = TweenBuilder()) {
        state.status = ScreenState.Status.MOVING
        animatedFloat.animateTo(0f, anim = anim, onEnd = { _, _ ->
            state.value = null
            state.status = ScreenState.Status.COLLAPSED
        })
    }
}


class SlideStackChildren<S>(private val screenState: ScreenState<S?>) {
    private val _slideScreenChildren = mutableListOf<@Composable() () -> Unit>()
    internal val slideScreenChildren: MutableList<@Composable() () -> Unit>
        get() = _slideScreenChildren

    infix fun S.to(children: @Composable() (ScreenState.Status) -> Unit) {
        _slideScreenChildren += {
            key(this@to) {
                if(this@to == screenState.value) {
                    ParentData(data = this@to!!, children = {
                        children(
                            if (screenState.value == this@to) screenState.status else ScreenState.Status.COLLAPSED
                        )
                    })
                }
            }
        }
    }

}