package com.crskdev.compose.pokedex.ui.pokedex

import android.widget.Toast
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.key
import androidx.compose.remember
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.AppBarIcon
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.Screen
import com.crskdev.compose.pokedex.ui.common.*
import com.crskdev.compose.pokedex.ui.common.router.NavigatorAmbient
import com.crskdev.compose.pokedex.ui.pokedex.filter.FabFilterMenu
import com.crskdev.compose.pokedex.ui.pokedex.filter.FilterResult
import com.crskdev.compose.pokedex.ui.pokedex.filter.GenerationNumber
import com.crskdev.compose.pokedex.ui.utils.imageResource
import com.crskdev.compose.pokedex.ui.utils.imageVectorResource
import com.crskdev.compose.pokedex.ui.utils.nativeColor

/**
 * Created by Cristian Pela on 05.01.2020.
 */
@Composable
fun PokedexScreen() {
    val items = remember {
        listOf(
            PokedexItemUI(
                id = 1,
                order = "#001",
                name = "Venusaur",
                type1 = "Grass",
                type2 = "Poisson",
                colorId = R.color.green,
                image = ResourceLocation.Res(R.drawable.venusaur)
            ),
            PokedexItemUI(
                id = 2,
                order = "#002",
                name = "Venusaur",
                type1 = "Grass",
                type2 = "Poisson",
                colorId = R.color.yellow,
                image = ResourceLocation.Res(R.drawable.venusaur)
            ),
            PokedexItemUI(
                id = 3,
                order = "#003",
                name = "Venusaur",
                type1 = "Grass",
                type2 = "Poisson",
                colorId = R.color.red,
                image = ResourceLocation.Res(R.drawable.venusaur)
            )
        )
    }
    val navigator = ambient(NavigatorAmbient)
    val context = ambient(ContextAmbient)
    PokedexScreen(items = items) {
        when (it) {
            PokedexScreenAction.Back -> navigator.pop()
            is PokedexScreenAction.Selected -> navigator.push(Screen.About(it.id))
            is PokedexScreenAction.Filter -> {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun PokedexScreen(items: List<PokedexItemUI>, onAction: (PokedexScreenAction) -> Unit = {}) {
    val padding = remember { LayoutPadding(left = 24.dp, right = 16.dp) }
    PokeballBgContainer {
        Column(modifier = LayoutGravity.Stretch) {
            PokedexToolbar(modifier = padding.copy(top = ambient(key = WindowInsetsAmbient).statusBarHeight), action = {
                when (it) {
                    PokedexToolbarActions.BACK -> onAction(PokedexScreenAction.Back)
                    PokedexToolbarActions.MENU -> TODO()
                }
            })
            Spacer(modifier = LayoutHeight(8.dp))
            Text(
                modifier = padding,
                text = "Pokedex",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = LayoutHeight(24.dp))
            PokedexTable(padding, items) {
                onAction(PokedexScreenAction.Selected(it))
            }
        }
        Container(
            modifier =
            LayoutGravity.BottomRight
        ) {
            FabFilterMenu {
                val action = when (it) {
                    FilterResult.All -> PokedexScreenAction.Filter.All
                    is FilterResult.Search -> PokedexScreenAction.Filter.Search(it.query)
                    FilterResult.Favorites -> PokedexScreenAction.Filter.Favorites
                    is FilterResult.Generation -> PokedexScreenAction.Filter.Generation(it.number)
                    else -> null

                }
                action?.run { onAction(this) }
            }
        }

    }
}

sealed class PokedexScreenAction {
    object Back : PokedexScreenAction()
    class Selected(val id: Int) : PokedexScreenAction()
    sealed class Filter : PokedexScreenAction() {
        object All : Filter()
        object Favorites : Filter()
        class Generation(val number: GenerationNumber) : Filter()
        class Search(val query: String) : Filter()
    }
}


@Composable
private fun PokedexToolbar(modifier: Modifier, action: (PokedexToolbarActions) -> Unit = {}) {
    Container(
        modifier = modifier + LayoutPadding(right = (-8).dp),
        height = 56.dp,
        expanded = true,
        alignment = Alignment.TopLeft
    ) {
        FlexRow(
            LayoutSize.Fill,
            mainAxisAlignment = MainAxisAlignment.SpaceBetween,
            crossAxisSize = SizeMode.Expand
        ) {
            inflexible {
                Container(
                    modifier = LayoutHeight.Fill,
                    alignment = Alignment.Center,
                    children = {
                        AppBarIcon(
                            icon = imageVectorResource(
                                R.drawable.ic_baseline_keyboard_backspace_24,
                                tint = (MaterialTheme.colors()).onSurface
                            )
                        ) { action(PokedexToolbarActions.BACK) }
                    }
                )
            }
            inflexible {
                Container(
                    modifier = LayoutHeight.Fill,
                    alignment = Alignment.Center,
                    children = {
                        AppBarIcon(
                            icon = imageVectorResource(
                                R.drawable.ic_baseline_menu_24,
                                tint = (MaterialTheme.colors()).onSurface
                            )
                        ) { action(PokedexToolbarActions.MENU) }
                    }
                )
            }

        }
    }
}


@Composable
fun PokedexTable(modifier: Modifier, items: List<PokedexItemUI>, onClick: (Int) -> Unit) {

    val pokeballBg = imageResource(R.drawable.pokeball, 80.dp)

    VerticalScroller(modifier = modifier + LayoutSize.Fill) {
        FlowRow(
            mainAxisSpacing = 10.dp,
            crossAxisSpacing = 10.dp
        ) {
            items.forEach {
                key(it.id) {
                    PokedexItem(pokeballImage = pokeballBg, item = it, onClick = {
                        onClick(it.id)
                    })
                }
            }
        }
    }

}

enum class PokedexToolbarActions { BACK, MENU }

@Composable
fun PokedexItem(pokeballImage: Image, item: PokedexItemUI, onClick: () -> Unit) {
    Ripple(bounded = true) {
        Clickable(onClick = onClick) {
            PokedexItemContainer(
                pokeballCache = pokeballImage,
                color = nativeColor(item.colorId)
            ) {
                Text(
                    modifier = LayoutGravity.TopRight + LayoutPadding(top = 10.dp, right = 8.dp),
                    text = item.order,
                    style = TextStyle(
                        color = Color.Black.copy(alpha = 0.12f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
                Column(
                    modifier = LayoutGravity.TopLeft + LayoutPadding(
                        left = 16.dp,
                        top = 16.dp
                    )
                ) {
                    Text(
                        text = item.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = LayoutHeight(10.dp))
                    PokemonTypeChip(type = item.type1)
                    Spacer(modifier = LayoutHeight(6.dp))
                    PokemonTypeChip(type = item.type2)
                }

                Container(
                    modifier = LayoutGravity.BottomRight + LayoutPadding(
                        bottom = 10.dp,
                        right = 5.dp
                    )
                ) {
                    LoadImage(
                        location = item.image,
                        size = 70.dp
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    ScreenDevices.HuaweiP10 {
        //        val items = listOf(
//            PokedexItemUI(
//                id = 1,
//                order = "#001",
//                name = "Venusaur",
//                type1 = "Grass",
//                type2 = "Poisson",
//                colorId = R.color.green,
//                image = ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            PokedexItemUI(
//                id = 2,
//                order = "#002",
//                name = "Venusaur",
//                type1 = "Grass",
//                type2 = "Poisson",
//                colorId = R.color.yellow,
//                image = ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            PokedexItemUI(
//                id = 3,
//                order = "#003",
//                name = "Venusaur",
//                type1 = "Grass",
//                type2 = "Poisson",
//                colorId = R.color.red,
//                image = ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            PokedexItemUI(
//                id = 2,
//                order = "#004",
//                name = "Venusaur",
//                type1 = "Grass",
//                type2 = "Poisson",
//                colorId = R.color.brown,
//                image = ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            PokedexItemUI(
//                id = 3,
//                order = "#005",
//                name = "Venusaur",
//                type1 = "Grass",
//                type2 = "Poisson",
//                colorId = R.color.indigo,
//                image = ResourceLocation.Res(R.drawable.venusaur)
//            )
//        )
//        PokedexScreen(items = items)
        FabFilterMenu(defaultIsExpanded = true)
    }
}