package com.crskdev.compose.pokedex.ui.common

import android.content.res.Configuration
import androidx.compose.Ambient
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.core.ConfigurationAmbient
import androidx.ui.core.WithDensity
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.unit.*

/**
 * Created by Cristian Pela on 04.01.2020.
 */

val WindowInsetsAmbient = Ambient.of<WindowInsets>()

@Composable
fun WithWindowInsets(children: @Composable() () -> Unit) {
    val windowInsets = ambient(WindowInsetsAmbient)
    val config = ambient(ConfigurationAmbient)
    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
        FlexColumn(modifier = LayoutSize.Fill) {
            inflexible {
                Spacer(modifier = LayoutHeight(windowInsets.statusBarHeight))
            }
            flexible(1f) {
                children()
            }
            inflexible {
                Spacer(modifier = LayoutHeight(windowInsets.navigationBarHeight))
            }
        }
    } else {
        FlexRow(modifier = LayoutSize.Fill) {
            inflexible {
                Spacer(modifier = LayoutWidth(windowInsets.statusBarHeight))
            }
            flexible(1f) {
                children()
            }
            inflexible {
                Spacer(modifier = LayoutWidth(windowInsets.navigationBarHeight))
            }
        }
    }

}

data class WindowInsets(val statusBarHeight: Dp, val navigationBarHeight: Dp)

@Composable
fun ScreenDevice(width: Px, height: Px, dpi: Float, children: @Composable() () -> Unit) {
    val deviceDensity = remember { Density(dpi / 160) }
    WithDensity {
        val (wdp, hdp) = remember(width, height) { width.toDp() to height.toDp() }
        WindowInsetsAmbient.Provider(
            value = WindowInsets(
                24.dp,
                48.dp
            )
        ) {
            // DensityAmbient.Provider(value = deviceDensity) {
            WithDensity {
                MaterialTheme {
                    Surface {
                        Container(
                            width = wdp,
                            height = hdp,
                            alignment = Alignment.TopLeft
                        ) {
                            children()
                        }
                    }
                }
                //    }
            }
        }
    }
}


object ScreenDevices {
    @Composable
    fun HuaweiP10(children: @Composable() () -> Unit) {
        ScreenDevice(
            width = 1080.px,
            height = 1920.px,
            dpi = 1f,
            children = children
        )
    }
}
