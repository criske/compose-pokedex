package com.crskdev.compose.pokedex.ui.dashboard

import androidx.compose.Composable
import androidx.compose.Pivotal
import androidx.ui.core.LayoutTag
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Container
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.Spacer
import androidx.ui.layout.constraintlayout.ConstraintLayout
import androidx.ui.layout.constraintlayout.ConstraintSet
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.ui.common.*

/**
 * Created by Cristian Pela on 03.02.2020.
 */

object DashboardBasicInfo {

    private enum class Tags {
        ORDER, NAME, CATEGORY, TYPE1, TYPE2
    }

    private fun Tags.asLayoutTag(): LayoutTag = LayoutTag(this)

    private val constraintSet = ConstraintSet {

        val order = tag(Tags.ORDER)
        val name = tag(Tags.NAME)
        val category = tag(Tags.CATEGORY)
        val type1 = tag(Tags.TYPE1)
        val type2 = tag(Tags.TYPE2)

        with(name) {
            top constrainTo parent.top
            left constrainTo parent.left
        }

        with(order) {
            top constrainTo parent.top
            top.margin = 4.dp
            right constrainTo parent.right
        }

        with(type1) {
            with(top) {
                constrainTo(name.bottom)
                margin = 8.dp
            }
            left constrainTo parent.left
            bottom constrainTo parent.bottom
        }

        with(type2) {
            with(left) {
                constrainTo(type1.right)
                margin = 4.dp
            }
            top constrainTo type1.top
            bottom constrainTo parent.bottom
        }

        with(category) {
            with(top) {
                constrainTo(name.bottom)
                margin = 8.dp
            }
            right constrainTo parent.right
            bottom constrainTo parent.bottom
        }

    }

    @Composable
    fun Content(@Pivotal data: Data, localThemeType: LocalThemeType, modifier: Modifier = Modifier.None) {
        Container(modifier = LayoutHeight(75.dp) + modifier) {
            ConstraintLayout(constraintSet = constraintSet) {
                val foregroundColor = localThemeType.onSurface()
                val textTheme = TextStyle(color = foregroundColor)
                Text(
                    modifier = Tags.NAME.asLayoutTag(),
                    text = data.name,
                    style = (MaterialTheme.typography()).h4.merge(textTheme)
                )
                Text(
                    modifier = Tags.ORDER.asLayoutTag(), text = data.id,
                    style = (MaterialTheme.typography()).subtitle1.merge(textTheme)
                )
                Chip(
                    modifier = Tags.TYPE1.asLayoutTag(),
                    text = data.type1,
                    textColor = foregroundColor,
                    bgColor = foregroundColor.copy(alpha = 0.2f)
                )
                if (data.type2 == null) {
                    Spacer(modifier = Tags.TYPE2.asLayoutTag())
                } else {
                    Chip(
                        modifier = Tags.TYPE2.asLayoutTag(),
                        text = data.type1,
                        textColor = foregroundColor,
                        bgColor = foregroundColor.copy(alpha = 0.2f)
                    )
                }
                Text(
                    modifier = Tags.CATEGORY.asLayoutTag(), text = data.category,
                    style = (MaterialTheme.typography()).subtitle2.merge(textTheme)
                )
            }
        }
    }

    data class Data(val id: String, val name: String, val category: String, val type1: String, val type2: String?)

}

@Preview("About Basic Info")
@Composable
fun DashboardBasicInfoPreview() {
    ScreenDevices.HuaweiP10 {
        val color = Color.Yellow
        Surface(color = color) {
            DashboardBasicInfo.Content(
                data = DashboardBasicInfo.Data(
                    "#001",
                    "Bulbasaur",
                    "Seed Pokemon",
                    "Grass",
                    "Poison"
                ),
                localThemeType = color.localThemeType
            )
        }
    }
}