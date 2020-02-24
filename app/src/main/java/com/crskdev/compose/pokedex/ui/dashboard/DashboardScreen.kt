package com.crskdev.compose.pokedex.ui.dashboard

import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.common.FreeLayout
import com.crskdev.compose.pokedex.ui.common.LoadImage
import com.crskdev.compose.pokedex.ui.common.WindowInsetsAmbient
import com.crskdev.compose.pokedex.ui.common.localThemeType
import com.crskdev.compose.pokedex.ui.common.router.NavigatorAmbient
import com.crskdev.compose.pokedex.ui.dashboard.bottomsheet.DashboardBottomSheet
import com.crskdev.compose.pokedex.ui.pokedex.ResourceLocation

/**
 * Created by Cristian Pela on 06.01.2020.
 */
object DashboardScreen {

    @Composable
    operator fun invoke(id: Int) {
        val data = remember {
            val color = Color(android.graphics.Color.parseColor("#FB6C6C"))
            PokemonData(
                1,
                "#001",
                "Charmander",
                color,
                "Lizard Pokemon",
                "Fire",
                null,
                ResourceLocation.Res(R.drawable.charmander),
                "The flame that burns at the tip of its tail is an indication of its emotions. The flame wavers when Charmander is enjoying itself. If the Pokémon becomes enraged, the flame burns fiercely. ",
                "1' 04 (0.70 cm) ",
                "13.2 lbs (6.9 kg) ",
                "87.5%",
                "12.5%",
                "Monster",
                "Fire",
                null,
                "64",
                listOf(
                    PokemonData.BaseStat("HP", "45", 0.45f),
                    PokemonData.BaseStat("Attack", "60", 0.6f),
                    PokemonData.BaseStat("Defense", "48", 0.48f),
                    PokemonData.BaseStat("Sp. Atk", "65", 0.65f),
                    PokemonData.BaseStat("Sp. Def", "65", 0.65f),
                    PokemonData.BaseStat("Speed", "45", 0.45f),
                    PokemonData.BaseStat("Total", "317", 317f / 500f)

                ),
                PokemonData.EvolutionChains(
                    listOf(
                        PokemonData.EvolutionChain(
                            PokemonData.EvolutionChain.Pokemon(
                                1,
                                "Bulbasaur",
                                ResourceLocation.Res(R.drawable.venusaur)
                            ),
                            PokemonData.EvolutionChain.Pokemon(
                                2,
                                "Ivysaur",
                                ResourceLocation.Res(R.drawable.venusaur)
                            ),
                            "Lvl 16"
                        )
                    ), PokemonData.EvolutionChain(
                        PokemonData.EvolutionChain.Pokemon(
                            1,
                            "Bulbasaur",
                            ResourceLocation.Res(R.drawable.venusaur)
                        ),
                        PokemonData.EvolutionChain.Pokemon(
                            2,
                            "Ivysaur",
                            ResourceLocation.Res(R.drawable.venusaur)
                        ),
                        "Lvl 16"
                    )
                ),
                true
            )
        }
        val navigator = ambient(key = NavigatorAmbient)
        Content(data = data) {
            when (it) {
                DashboardAction.Back -> navigator.pop()
                is DashboardAction.Favorite -> {
                }
            }
        }
    }

    @Composable
    private fun Content(data: PokemonData, onAction: (DashboardAction) -> Unit = {}) {
        val windowInsets = ambient(WindowInsetsAmbient)
        Surface(color = data.color) {
            FreeLayout(modifier = LayoutSize.Fill) {
                Column {
                    DashboardToolbar.Content(
                        modifier = LayoutWidth.Fill + LayoutPadding(
                            left = 16.dp,
                            right = 16.dp,
                            top = windowInsets.statusBarHeight
                        ),
                        isFavorite = data.isFavorite,
                        localThemeType = data.color.localThemeType
                    ) {
                        val action = when (it) {
                            DashboardToolbar.Action.Back -> DashboardAction.Back
                            is DashboardToolbar.Action.Favorite -> DashboardAction.Favorite(
                                it.value
                            )
                        }
                        onAction(action)
                    }
                    DashboardBasicInfo.Content(
                        modifier = LayoutWidth.Fill + LayoutPadding(
                            left = 16.dp,
                            right = 16.dp
                        ),
                        data = data.toDashboardBasicInfo(),
                        localThemeType = data.color.localThemeType
                    )
                    Spacer(LayoutHeight(150.dp))
                    DashboardBottomSheet.Content(
                        modifier = LayoutSize.Fill,
                        data = data
                    )
                }
                Container(
                    modifier = FreePosition(alignment = Alignment.Center, y = (-100).dp)
                ) {
                    LoadImage(location = data.image, width = 200.dp, height = 220.dp)
                }
            }
        }
    }

    internal sealed class DashboardAction {
        object Back : DashboardAction()
        class Favorite(val value: Boolean) : DashboardAction()
    }

    private fun PokemonData.toDashboardBasicInfo(): DashboardBasicInfo.Data =
        DashboardBasicInfo.Data(
            id = idStr,
            name = name,
            category = category,
            type1 = type1,
            type2 = type2
        )

}


