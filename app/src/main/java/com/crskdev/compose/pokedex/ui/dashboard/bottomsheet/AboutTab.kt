package com.crskdev.compose.pokedex.ui.dashboard.bottomsheet

import androidx.compose.Composable
import androidx.ui.core.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.px
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.common.LoadImage
import com.crskdev.compose.pokedex.ui.common.ScreenDevices
import com.crskdev.compose.pokedex.ui.pokedex.ResourceLocation

/**
 * Created by Cristian Pela on 03.02.2020.
 */
object AboutTab {
    @Composable
    fun Content(data: Data) {
        CurrentTextStyleProvider(defaultTypography()) {
            Column {
                Text(
                    modifier = LayoutWidth.Fill,
                    text = data.description,
                    style = currentTextStyle().merge(TextStyle(textAlign = TextAlign.Justify))
                )

                Spacer(modifier = LayoutHeight(24.dp))
                HeightWeightSection(height = data.height, weight = data.weight)
                Spacer(modifier = LayoutHeight(24.dp))

                Text(text = "Breeding", style = (MaterialTheme.typography()).subtitle1)
                Spacer(modifier = LayoutHeight(8.dp))
                BreedingSection(
                    male = data.genderProportions.male,
                    female = data.genderProportions.female,
                    eggGroups = data.eggGroups,
                    eggCycle = data.eggCycle
                )
                Spacer(modifier = LayoutHeight(24.dp))

                Text(text = "Location", style = (MaterialTheme.typography()).subtitle1)
                Spacer(modifier = LayoutHeight(8.dp))
                LocationSection(location = data.location)
                Spacer(modifier = LayoutHeight(24.dp))

                Text(text = "Training", style = (MaterialTheme.typography()).subtitle1)
                Spacer(modifier = LayoutHeight(8.dp))
                TrainingSection(exp = data.baseExp)
            }
        }
    }

    @Composable
    private fun HeightWeightSection(height: String, weight: String) {
        Surface(elevation = 3.dp, shape = RoundedCornerShape(12.px), modifier = LayoutWidth.Fill) {
            Container(
                padding = EdgeInsets(
                    left = 24.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                    right = 24.dp
                )
            ) {
                Table(columns = 2) {
                    tableRow {
                        Header(text = "Height")
                        Header(text = "Weight")
                    }
                    tableRow {
                        Spacer(modifier = LayoutHeight(16.dp))
                    }
                    tableRow {
                        Text(text = height)
                        Text(text = weight)
                    }
                }
            }
        }
    }

    @Composable
    private fun BreedingSection(male: String, female: String, eggGroups: String, eggCycle: String) {
        Table(columns = 3) {
            tableRow {
                Header(text = "Gender")
                Text(text = "\u2642 $male")
                Text(text = "♀ $female")
            }
            tableRow {
                Spacer(modifier = LayoutHeight(8.dp))
            }
            tableRow {
                Header(text = "Egg Groups")
                Text(text = eggGroups)
            }
            tableRow {
                Spacer(modifier = LayoutHeight(8.dp))
            }
            tableRow {
                Header(text = "Egg Cycle")
                Text(text = eggCycle)
            }
        }
    }

    @Composable
    private fun LocationSection(location: ResourceLocation?) {
        WithDensity {
            Clip(shape = RoundedCornerShape(12.px)) {
                if (location == null) {
                    Surface(
                        color = Color.Gray,
                        modifier = LayoutSize(320.px.toDp(), 142.px.toDp())
                    ) {}
                } else {
                    LoadImage(location = location, width = 320.dp, height = 142.dp)
                }
            }
        }
    }

    @Composable
    private fun TrainingSection(exp: String) {
        Table(columns = 3) {
            tableRow {
                Header(text = "Base EXP")
                Text(text = exp)
            }
        }
    }


    @Composable
    private fun Header(text: String) {
        Opacity(opacity = 0.4f) { Text(text = text) }
    }

    @Composable
    private fun defaultTypography(): TextStyle = (MaterialTheme.typography()).body2

    data class Data(
        val description: String,
        val height: String,
        val weight: String,
        val genderProportions: GenderProportions,
        val eggGroups: String,
        val eggCycle: String,
        val location: ResourceLocation?,
        val baseExp: String
    ) {
        data class GenderProportions(val male: String, val female: String)
    }
}

@Preview
@Composable
fun AboutTabPreview() {

    ScreenDevices.HuaweiP10 {
        val data = AboutTab.Data(
            description = "Bulbasaur can be seen napping in bright sunlight. There is a seed on its back. By soaking up the sun's rays, the seed grows progressively larger.",
            height = "2’3.6” (0.70 cm)",
            weight = " 15.2 lbs (6.9 kg)",
            genderProportions = AboutTab.Data.GenderProportions(
                male = "87.5%",
                female = "12.5%"
            ),
            eggGroups = "Monster",
            eggCycle = "Grass",
            location = ResourceLocation.Res(R.drawable.bulbasaur_location),
            baseExp = 64.toString()
        )
        Padding(padding = 16.dp) {
            AboutTab.Content(data = data)
        }

    }


}