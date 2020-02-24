package com.crskdev.compose.pokedex.ui.dashboard.bottomsheet

import androidx.compose.Composable
import androidx.compose.Pivotal
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import androidx.ui.material.lightColorPalette
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import androidx.ui.unit.px
import com.crskdev.compose.pokedex.ui.dashboard.PokemonData

/**
 * Created by Cristian Pela on 03.02.2020.
 */

object DashboardBottomSheet {

    @Composable
    fun Content(@Pivotal data: PokemonData, modifier: Modifier = Modifier.None) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(topLeft = 30.px, topRight = 30.px),
            color = Color.White
        ) {
            Column(
                modifier = modifier + LayoutPadding(
                    left = 16.dp,
                    right = 16.dp,
                    top = 50.dp
                )
            ) {
                var selectedTab by state { Tabs.ABOUT }
                MaterialTheme(
                    colors = lightColorPalette(
                        primary = Color.White,
                        onPrimary = data.color.copy(alpha = 0.4f)
                    )
                ) {
                    TabRow(
                        items = Tabs.values().asList(),
                        selectedIndex = selectedTab.ordinal
                    ) { index, tab ->
                        Tab(text = tab.title, selected = selectedTab.ordinal == index) {
                            selectedTab = Tabs.values()[index]
                        }
                    }
                }
                Spacer(modifier = LayoutHeight(16.dp))
                Container(alignment = Alignment.TopLeft) {
                    Crossfade(current = selectedTab) {
                        when (it) {
                            Tabs.ABOUT -> VerticalScroller {
                                AboutTab.Content(data.toAboutData())
                            }
                            Tabs.BASE_STATS -> VerticalScroller {
                                Center {
                                    BaseStatsTab.Content(data.toBaseStatsData())
                                }
                            }
                            Tabs.EVO -> VerticalScroller {
                                EvolutionTab(data.toEvolutionsChainsData())
                            }
                            Tabs.MOVES -> Wrap {}
                        }
                    }
                }
            }
        }
    }


    private enum class Tabs(val title: String) {
        ABOUT("About"), BASE_STATS("Base Stats"), EVO("Evolution"), MOVES("Moves")
    }

    private fun PokemonData.toAboutData(): AboutTab.Data = AboutTab.Data(
        description,
        height,
        weight,
        AboutTab.Data.GenderProportions(maleProp, femaleProp),
        eggGroups,
        eggCycle,
        location,
        baseExp
    )

    private fun PokemonData.toBaseStatsData(): BaseStatsTab.Data = BaseStatsTab.Data(
        stats.map {
            BaseStatsTab.Data.Stat(it.name, it.value, it.progress)
        }
    )

    private fun PokemonData.toEvolutionsChainsData(): EvolutionTab.Data =
        with(evolutionChains) {
            EvolutionTab.Data(
                normal.map {
                    EvolutionTab.Data.EvolutionChain(
                        EvolutionTab.Data.EvolutionChain.Pokemon(
                            it.from.id,
                            it.from.name,
                            it.from.image
                        ),
                        EvolutionTab.Data.EvolutionChain.Pokemon(it.to.id, it.to.name, it.to.image),
                        it.level
                    )
                },
                EvolutionTab.Data.EvolutionChain(
                    EvolutionTab.Data.EvolutionChain.Pokemon(
                        mega.from.id,
                        mega.from.name,
                        mega.from.image
                    ),
                    EvolutionTab.Data.EvolutionChain.Pokemon(
                        mega.to.id,
                        mega.to.name,
                        mega.to.image
                    ),
                    mega.level
                )
            )
        }
}