package com.crskdev.compose.pokedex.ui.pokedex.filter

import androidx.compose.*
import androidx.ui.core.Alignment
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.common.*
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandler
import com.crskdev.compose.pokedex.ui.common.router.BackstackHandlerAmbient
import com.crskdev.compose.pokedex.ui.utils.*

/**
 * Created by Cristian Pela on 29.01.2020.
 */
sealed class FilterResult {
    object None : FilterResult()
    object All : FilterResult()
    class Search(val query: String) : FilterResult()
    object Favorites : FilterResult()
    class Generation(val number: GenerationNumber) : FilterResult()
}

enum class GenerationNumber {
    I, II, III, IV, V, VI, VII, VIII
}

enum class FilterSlideScreenType {
    None, Search, Generation
}

@Composable
fun FabFilterMenu(defaultIsExpanded: Boolean = false, onActionResult: (FilterResult) -> Unit = {}) {
    var isExpanded by state { defaultIsExpanded }
    val windowInsets = ambient(WindowInsetsAmbient)
    Stack {
        Surface(
            modifier = LayoutGravity.Stretch,
            color = if (isExpanded) Color.Gray.copy(alpha = 0.4f) else Color.Transparent
        ) {
            Container {}
        }
        if (isExpanded) {
            Container(modifier = LayoutGravity.Stretch) {
                FilterSlideScreen {
                    onActionResult(it)
                    isExpanded = false
                }
            }

        } else {
            FloatingActionButton(
                modifier = LayoutGravity.BottomRight + LayoutPadding(
                    right = 16.dp,
                    bottom = 16.dp + windowInsets.navigationBarHeight
                ),
                icon = imageVectorResource(
                    id = R.drawable.ic_baseline_filter_list_24,
                    tint = Color.White
                ),
                color = nativeColor(R.color.indigo),
                onClick = { isExpanded = true }
            )
        }
    }
}

@Composable
private fun FilterSlideScreen(onClose: (FilterResult) -> Unit) {

    val routerName = remember { "SlideScreen" }

    val controlArea: @Composable() (SlideController<FilterSlideScreenType>) -> Unit =
        { controller ->

            val backstackHandler = ambient(BackstackHandlerAmbient)

            onActive {
                val sub =
                    backstackHandler.attachRouter(
                        routerName,
                        FilterSlideScreenType.None
                    ) { ev, screen ->
                        when (ev) {
                            BackstackHandler.BackstackEventType.PUSH -> (screen as FilterSlideScreenType)
                                .takeIf { it != FilterSlideScreenType.None }
                                ?.run { controller.changeScreen(this) }
                            is BackstackHandler.BackstackEventType.POP -> {
                                if (ev.data is FilterResult) {
                                    onClose(ev.data)
                                } else {
                                    controller.collapse()
                                }
                            }
                            else -> Unit
                        }
                    }
                onDispose {
                    sub.clear()
                }
            }

            Clickable(consumeDownOnStart = true, onClick = {
                when (controller.state().status) {
                    ScreenState.Status.EXPANDED -> backstackHandler.pop(
                        BackstackHandler.BackPressedEventSource.USER,
                        routerName
                    )
                    ScreenState.Status.COLLAPSED -> onClose(FilterResult.None)
                    ScreenState.Status.MOVING -> Unit
                }
            }) {
                Container(expanded = true, alignment = Alignment.BottomRight) {
                    if (controller.state().status == ScreenState.Status.COLLAPSED)
                        Padding(padding = 16.dp) {
                            FlexColumn(
                                crossAxisAlignment = CrossAxisAlignment.End,
                                modifier = LayoutWidth.Fill
                            ) {
                                inflexible {
                                    FabFilterMenuButton(
                                        text = "Favorite Pokemon",
                                        icon = imageVectorResource(
                                            id = R.drawable.ic_baseline_favorite_24
                                        ),
                                        onClick = {
                                            backstackHandler.pop(
                                                BackstackHandler.BackPressedEventSource.USER,
                                                routerName,
                                                FilterResult.Favorites
                                            )
                                        }
                                    )
                                    Spacer(modifier = LayoutHeight(8.dp))
                                }

                                inflexible {
                                    FabFilterMenuButton(
                                        text = "All Type",
                                        icon = imageResource(R.drawable.pokeball, 24.dp),
                                        onClick = {
                                            backstackHandler.pop(
                                                BackstackHandler.BackPressedEventSource.USER,
                                                routerName,
                                                FilterResult.All
                                            )
                                        }
                                    )
                                    Spacer(modifier = LayoutHeight(8.dp))
                                }
                                inflexible {
                                    FabFilterMenuButton(
                                        text = "All Gen",
                                        icon = imageResource(R.drawable.pokeball, 24.dp),
                                        onClick = {
                                            backstackHandler.push(
                                                routerName,
                                                FilterSlideScreenType.Generation
                                            )
                                        }
                                    )
                                    Spacer(modifier = LayoutHeight(8.dp))
                                }
                                inflexible {
                                    FabFilterMenuButton(
                                        text = "Search",
                                        icon = imageVectorResource(
                                            id = R.drawable.ic_baseline_search_24
                                        ),
                                        onClick = {
                                            backstackHandler.push(
                                                routerName,
                                                FilterSlideScreenType.Search
                                            )
                                        }
                                    )
                                    Spacer(modifier = LayoutHeight(8.dp))
                                }

                                inflexible {
                                    FloatingActionButton(
                                        icon = imageVectorResource(
                                            id = R.drawable.ic_baseline_close_24,
                                            tint = Color.White
                                        ),
                                        color = nativeColor(R.color.indigo),
                                        onClick = {
                                            onClose(FilterResult.None)
                                        }
                                    )
                                }
                            }
                        }
                }
            }

        }
    Window(onCloseRequest = { onClose(FilterResult.None) }) {

        val backstackHandler = ambient(BackstackHandlerAmbient)

        WithWindowInsets {
            Container(expanded = true, alignment = Alignment.TopLeft) {
                SlideStack(controlArea = controlArea) {
                    FilterSlideScreenType.Search to {
                        FilterSearchScreen {
                            backstackHandler.pop(
                                BackstackHandler.BackPressedEventSource.USER,
                                routerName,
                                FilterResult.Search(it)
                            )
                        }
                    }
                    FilterSlideScreenType.Generation to {
                        FilterGenerationScreen {
                            backstackHandler.pop(
                                BackstackHandler.BackPressedEventSource.USER,
                                routerName,
                                FilterResult.Generation(it)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FabFilterMenuButton(text: String, icon: Image, onClick: () -> Unit = {}) {
    Clickable(onClick = onClick) {
        Surface(shape = RoundedCornerShape(33.dp), color = Color.White) {
            Container(
                modifier = Spacing(
                    left = 24.dp,
                    right = 16.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
            ) {
                FlowRow(
                    mainAxisSpacing = 8.dp,
                    crossAxisAlignment = FlowCrossAxisAlignment.Center
                ) {
                    Text(text = text, style = TextStyle(fontSize = 14.sp))
                    DrawImage(icon, size = 24.dp, tint = nativeColor(R.color.indigo))
                }
            }
        }
    }
}




