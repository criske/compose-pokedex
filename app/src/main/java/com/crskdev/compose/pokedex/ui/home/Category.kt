package com.crskdev.compose.pokedex.ui.home

import android.graphics.Bitmap
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.core.Layout
import androidx.ui.core.Text
import androidx.ui.core.ambientDensity
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
import androidx.ui.material.surface.Surface
import androidx.ui.res.stringResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.*
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.common.PokeballBgContainer
import com.crskdev.compose.pokedex.ui.common.PokemonSearchField
import com.crskdev.compose.pokedex.ui.utils.*

/**
 * Created by Cristian Pela on 03.01.2020.
 */
@Composable
fun Category(onAction: (HomeAction) -> Unit = {}) {
    Column {
        Wrap {
            Card(shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp), elevation = 2.dp) {
                PokeballBgContainer(modifier = LayoutHeight(580.dp)) {
                    Column(
                        modifier = LayoutGravity.Stretch + LayoutPadding(
                            left = 28.dp,
                            right = 28.dp,
                            bottom = 60.dp,
                            top = 117.dp
                        ),
                        arrangement = Arrangement.SpaceAround
                    ) {
                        Title()
                        Spacer(modifier = LayoutHeight(42.dp))
                        PokemonSearchField { onAction(HomeAction.SearchItem(it)) }
                        Spacer(modifier = LayoutHeight(42.dp))
                        Align(alignment = Alignment.Center) { CategoryTable(onClick = onAction) }
                    }
                }
            }
        }
    }
}


@Composable
private fun CategoryButton(color: Color,
                           pokeballTint: Color,
                           pokeballImageLeft: Image,
                           pokeballImageRight: Image,
                           action: HomeAction.Category,
                           onClick: (HomeAction.Category) -> Unit = {}) {
    Surface(color = color, shape = RoundedCornerShape(20.dp), elevation = 10.dp) {
        Ripple(bounded = true, color = Color.White, enabled = true) {
            Clickable(onClick = { onClick(action) }) {
                val allChildren = @Composable() {
                    DrawImage(image = pokeballImageLeft, tint = pokeballTint, size = 80.dp)
                    DrawImage(image = pokeballImageRight, tint = pokeballTint, size = 64.dp)
                    Text(
                        text = (stringResource(action.title)).capitalizeEach(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    )
                }
                Layout(
                    modifier = LayoutSize(140.dp, 64.dp),
                    children = allChildren
                ) { measurables, constraints ->
                    val (ballLeft, ballRight, text) = measurables
                        .map {
                            it.measure(constraints.copy(minWidth = 0.ipx, minHeight = 0.ipx))
                        }
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        text.place(
                            Alignment.CenterLeft.align(text.size)
                                    + IntPxPosition(16.dp.toIntPx(), text.height / 2)
                        )
                        ballLeft.place(
                            Alignment.TopLeft.align(ballLeft.size)
                                    - IntPxPosition(35.dp.toIntPx(), 50.dp.toIntPx())

                        )
                        ballRight.place(
                            Alignment.TopRight.align(ballRight.size)
                                    + IntPxPosition(25.dp.toIntPx(), IntPx.Zero)

                        )

                    }
                }

            }

        }
    }
}


@Composable
private fun Title() {
    Text(
        text = stringResource(R.string.home_screen_title),
        style = TextStyle(
            lineHeight = 42.sp,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
//            shadow = Shadow(
//                color = (nativeColor(R.color.grey)).copy(alpha = 0.65f),
//                offset = Offset(10f, 2f),
//                blurRadius = 15.px
//            )
        ),
        softWrap = true
    )
}

@Composable
private fun CategoryTable(onClick: (HomeAction.Category) -> Unit = {}) {
    //cache the pokeball button images first, for better performance
    val density = ambientDensity()
    val pokeballImageRight = imageResource(R.drawable.pokeball, 80.dp)
    val pokeballImageLeft = remember {
        withDensity(density) {
            val scaled = Bitmap.createScaledBitmap(
                pokeballImageRight.nativeImage,
                64.dp.toIntPx().value,
                64.dp.toIntPx().value,
                true
            )
            AndroidImage(scaled)
        }
    }
    val pokeballTint = remember { Color.White.copy(alpha = 0.15f) }

    Table(columns = 2, columnWidth = { TableColumnWidth.Wrap }) {
        tableRow {
            Padding(padding = EdgeInsets(right = 10.dp)) {
                CategoryButton(
                    color = nativeColor(R.color.teal),
                    pokeballTint = pokeballTint,
                    pokeballImageLeft = pokeballImageLeft,
                    pokeballImageRight = pokeballImageRight,
                    action = HomeAction.Category.Pokedex,
                    onClick = onClick
                )
            }
            Padding(padding = EdgeInsets(bottom = 10.dp)) {
                CategoryButton(
                    color = nativeColor(R.color.red),
                    pokeballTint = pokeballTint,
                    pokeballImageLeft = pokeballImageLeft,
                    pokeballImageRight = pokeballImageRight,
                    action = HomeAction.Category.Moves,
                    onClick = onClick
                )
            }
        }
        tableRow {
            Padding(padding = EdgeInsets(right = 10.dp)) {
                CategoryButton(
                    color = nativeColor(R.color.blue),
                    pokeballTint = pokeballTint,
                    pokeballImageLeft = pokeballImageLeft,
                    pokeballImageRight = pokeballImageRight,
                    action = HomeAction.Category.Abilities,
                    onClick = onClick
                )
            }
            Padding(padding = EdgeInsets(bottom = 10.dp)) {
                CategoryButton(
                    color = nativeColor(R.color.yellow),
                    pokeballTint = pokeballTint,
                    pokeballImageLeft = pokeballImageLeft,
                    pokeballImageRight = pokeballImageRight,
                    action = HomeAction.Category.Items,
                    onClick = onClick
                )
            }
        }
        tableRow {
            Padding(padding = EdgeInsets(bottom = 10.dp)) {
                CategoryButton(
                    color = nativeColor(R.color.indigo),
                    pokeballTint = pokeballTint,
                    pokeballImageLeft = pokeballImageLeft,
                    pokeballImageRight = pokeballImageRight,
                    action = HomeAction.Category.Locations,
                    onClick = onClick
                )
            }
            CategoryButton(
                color = nativeColor(R.color.brown),
                pokeballTint = pokeballTint,
                pokeballImageLeft = pokeballImageLeft,
                pokeballImageRight = pokeballImageRight,
                action = HomeAction.Category.TypeCharts,
                onClick = onClick
            )
        }
    }
}


@Preview
@Composable
fun Test() {
    val density = ambientDensity()
    val pokeballImageRight = imageResource(R.drawable.pokeball, 80.dp)
    val pokeballImageLeft = remember {
        withDensity(density) {
            val scaled = Bitmap.createScaledBitmap(
                pokeballImageRight.nativeImage,
                64.dp.toIntPx().value,
                64.dp.toIntPx().value,
                false
            )
            AndroidImage(scaled)
        }
    }
    val pokeballTint = remember { Color.White.copy(alpha = 0.15f) }

    MaterialTheme {
        Container(width = 400.dp, height = 400.dp) {
            CategoryButton(
                color = Color.Red,
                pokeballTint = pokeballTint,
                pokeballImageLeft = pokeballImageLeft,
                pokeballImageRight = pokeballImageRight,
                action = HomeAction.Category.TypeCharts
            )
        }
    }

}