package com.crskdev.compose.pokedex.ui.dashboard.bottomsheet

import androidx.annotation.FloatRange
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.core.CurrentTextStyleProvider
import androidx.ui.core.Opacity
import androidx.ui.core.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.ui.common.ScreenDevices

/**
 * Created by Cristian Pela on 03.02.2020.
 */
object BaseStatsTab {

    @Composable
    fun Content(data: Data) {
        CurrentTextStyleProvider((MaterialTheme.typography()).body2) {
            Table(columns = 5,
                alignment = { Alignment.CenterLeft },
                columnWidth = {
                    if (it == 4) {
                        TableColumnWidth.Flex(1f)
                    } else {
                        TableColumnWidth.Wrap
                    }
                }) {
                data.entries.forEach {
                    tableRow {
                        Opacity(opacity = 0.4f) { Text(text = it.name) }
                        Spacer(modifier = LayoutWidth(16.dp))
                        Text(text = it.value)
                        Spacer(modifier = LayoutWidth(16.dp))
                        Progress(progress = it.progress)
                    }
                }
            }
        }
    }

    @Composable
    private fun Progress(progress: Float) {
        Container(modifier = LayoutHeight(30.dp)) {
            LinearProgressIndicator(
                progress = progress,
                color = if (progress < 0.5) Color.Red else Color.Green
            )
        }
    }

    data class Data(val entries: List<Stat>){
        data class Stat(val name: String,
                        val value: String,
                        @FloatRange(from = 0.0, to = 1.0) val progress: Float)
    }



}

@Preview
@Composable
fun BaseStatsTabPreview() {
    ScreenDevices.HuaweiP10 {
        val data = remember {
            BaseStatsTab.Data(
                listOf(
                    BaseStatsTab.Data.Stat("HP", "45", 0.45f),
                    BaseStatsTab.Data.Stat("Attack", "60", 0.6f),
                    BaseStatsTab.Data.Stat("Defense", "48", 0.48f),
                    BaseStatsTab.Data.Stat("Sp. Atk", "65", 0.65f),
                    BaseStatsTab.Data.Stat("Sp. Def", "65", 0.65f),
                    BaseStatsTab.Data.Stat("Speed", "45", 0.45f),
                    BaseStatsTab.Data.Stat("Total", "317", 317f / 500f)
                )
            )
        }
        BaseStatsTab.Content(data = data)
    }
}
