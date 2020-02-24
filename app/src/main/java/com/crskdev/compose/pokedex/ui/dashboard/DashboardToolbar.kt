package com.crskdev.compose.pokedex.ui.dashboard

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.AppBarIcon
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.common.LocalThemeType
import com.crskdev.compose.pokedex.ui.common.ScreenDevices
import com.crskdev.compose.pokedex.ui.common.localThemeType
import com.crskdev.compose.pokedex.ui.common.onSurface
import com.crskdev.compose.pokedex.ui.utils.imageVectorResource

/**
 * Created by Cristian Pela on 03.02.2020.
 */
object DashboardToolbar {

    @Composable
    internal fun Content(modifier: Modifier = Modifier.None,
                         isFavorite: Boolean,
                         localThemeType: LocalThemeType,
                         onAction: (Action) -> Unit = {}) {

        Row(
            modifier = modifier + LayoutHeight(56.dp),
            arrangement = Arrangement.SpaceBetween
        ) {
            Container(modifier = LayoutGravity.Center) {
                AppBarIcon(icon = imageVectorResource(
                    R.drawable.ic_baseline_keyboard_backspace_24,
                    tint = localThemeType.onSurface()
                ), onClick = { onAction(Action.Back) })
            }
            Container(modifier = LayoutGravity.Center) {
                AppBarIcon(icon = imageVectorResource(
                    if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24,
                    tint = localThemeType.onSurface()
                ), onClick = { onAction(Action.Favorite(!isFavorite)) })
            }
        }
    }

    internal sealed class Action {
        object Back : Action()
        class Favorite(val value: Boolean) : Action()
    }

}

@Preview("About - Toolbar")
@Composable
fun AboutToolbarPreview() {
    ScreenDevices.HuaweiP10 {
        Surface(color = Color.Green) {
            DashboardToolbar.Content(
                isFavorite = false,
                localThemeType = Color.Green.localThemeType
            )
        }
    }
}