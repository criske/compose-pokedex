package com.crskdev.compose.pokedex.ui.dashboard.bottomsheet

import androidx.compose.Composable
import androidx.ui.core.LayoutTag
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.DrawImage
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.layout.constraintlayout.ConstraintLayout
import androidx.ui.layout.constraintlayout.ConstraintSet
import androidx.ui.layout.constraintlayout.ConstraintSetBuilderScope
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.res.*
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.dashboard.bottomsheet.EvolutionTab.EvoChainTag.*
import com.crskdev.compose.pokedex.ui.pokedex.ResourceLocation
import com.crskdev.compose.pokedex.ui.utils.DrawImage
import com.crskdev.compose.pokedex.ui.utils.imageVectorResource

/**
 * Created by Cristian Pela on 06.02.2020.
 */
object EvolutionTab {

    @Composable
    operator fun invoke(evolutionsChains: Data) {
        Content(evolutionsChains = evolutionsChains)
    }

    @Composable
    private fun Content(evolutionsChains: Data) {
        Column(modifier = LayoutSize.Fill) {
            Text(text = "Evolution Chain", style = (MaterialTheme.typography()).subtitle2)
            evolutionsChains.normal.forEachIndexed { index, item ->
                EvolutionChainItem(item)
                if (index < evolutionsChains.normal.lastIndex) {
                    Divider(color = MaterialTheme.colors().onSurface.copy(alpha = 0.05f))
                }
            }
            Text(text = "Evolution Chain", style = (MaterialTheme.typography()).subtitle2)
        }
    }


    private val evolutionChainItemConstraintSet = ConstraintSet {
        val pokeballLeft = tag(POKEBALL_LEFT)
        val pokeballRight = tag(POKEBALL_RIGHT)
        val from = tag(FROM)
        val fromName = tag(FROM_NAME)
        val to = tag(TO)
        val toName = tag(TO_NAME)
        val level = tag(LEVEL)
        val arrow = tag(ARROW)

        //left side
        with(pokeballLeft) {
            top constrainTo parent.top
            left constrainTo parent.left
            bottom constrainTo parent.bottom
        }
        with(from) {
            left constrainTo pokeballLeft.left
            right constrainTo pokeballLeft.right
            top constrainTo pokeballLeft.top
            bottom constrainTo pokeballLeft.bottom
        }

        with(fromName) {
            top constrainTo pokeballLeft.bottom
            top.margin = 4.dp
            left constrainTo pokeballLeft.left
            right constrainTo pokeballLeft.right
        }

        //right side
        with(pokeballRight) {
            top constrainTo parent.top
            right constrainTo parent.right
            bottom constrainTo parent.bottom
        }
        with(to) {
            left constrainTo pokeballRight.left
            right constrainTo pokeballRight.right
            top constrainTo pokeballRight.top
            bottom constrainTo pokeballRight.bottom
        }

        with(toName) {
            top constrainTo pokeballRight.bottom
            top.margin = 4.dp
            left constrainTo pokeballRight.left
            right constrainTo pokeballRight.right
        }

        with(arrow) {
            top constrainTo parent.top
            left constrainTo pokeballLeft.right
            right constrainTo pokeballRight.left
        }

        with(level) {
            bottom constrainTo parent.bottom
            top constrainTo arrow.bottom
            top.margin = 4.dp
            left constrainTo pokeballLeft.right
            right constrainTo pokeballRight.left
        }

        createVerticalChain(arrow, level, chainStyle = ConstraintSetBuilderScope.ChainStyle.Packed)
    }

    private enum class EvoChainTag {
        POKEBALL_LEFT,
        POKEBALL_RIGHT,
        FROM,
        TO,
        LEVEL,
        ARROW,
        FROM_NAME,
        TO_NAME;

        fun toLayoutTag() = LayoutTag(this)
    }


    @Composable
    internal fun EvolutionChainItem(evolution: Data.EvolutionChain) {
        Container(modifier = LayoutWidth.Fill + LayoutHeight(100.dp)) {
            ConstraintLayout(evolutionChainItemConstraintSet) {
                AsyncImage(
                    modifier = POKEBALL_LEFT.toLayoutTag(),
                    deferred = loadImageResource(id = R.drawable.pokeball_small),
                    size = 100.dp to 100.dp
                )
                AsyncImage(
                    modifier = FROM.toLayoutTag(),
                    deferred = loadImageResource(id = (evolution.from.image as ResourceLocation.Res).id),
                    size = 80.dp to 80.dp
                )
                Text(
                    modifier = FROM_NAME.toLayoutTag(),
                    text = evolution.from.name,
                    style = MaterialTheme.typography().body2
                )

                Container(modifier = ARROW.toLayoutTag(), width = 24.dp, height = 24.dp) {
                    DrawImage(
                        image = imageVectorResource(id = R.drawable.ic_baseline_arrow_right_alt_24),
                        tint = Color.Gray.copy(alpha = 0.5f)
                    )
                }
                Text(
                    modifier = LEVEL.toLayoutTag(),
                    text = evolution.level,
                    style = MaterialTheme.typography().caption
                )


                AsyncImage(
                    modifier = POKEBALL_RIGHT.toLayoutTag(),
                    deferred = loadImageResource(id = R.drawable.pokeball_small),
                    size = 100.dp to 100.dp
                )
                AsyncImage(
                    modifier = TO.toLayoutTag(),
                    deferred = loadImageResource(id = (evolution.to.image as ResourceLocation.Res).id),
                    size = 80.dp to 80.dp
                )
                Text(
                    modifier = TO_NAME.toLayoutTag(),
                    text = evolution.to.name,
                    style = MaterialTheme.typography().body2
                )

            }
        }
    }

    @Composable
    private fun AsyncImage(modifier: Modifier, deferred: DeferredResource<Image>, size: Pair<Dp, Dp>, tint: Color? = null) {
        Container(modifier = modifier) {
            when (deferred.resource) {
                is LoadedResource -> DrawImage(
                    image = deferred.resource.resource!!,
                    width = size.first,
                    height = size.second,
                    tint = tint
                )
                is PendingResource -> Wrap {}
                is FailedResource -> Wrap {}
            }
        }
    }


    data class Data(val normal: List<EvolutionChain>, val mega: EvolutionChain) {
        data class EvolutionChain(val from: Pokemon, val to: Pokemon, val level: String) {
            data class Pokemon(val id: Int, val name: String, val image: ResourceLocation)
        }
    }
}

//@Preview
//@Composable
//fun EvolutionChainItemPreview() {
//    val evolution = remember {
//        EvolutionTab.Data.EvolutionChain(
//            EvolutionTab.Data.EvolutionChain.Pokemon(
//                1,
//                "Bulbasaur",
//                ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            EvolutionTab.Data.EvolutionChain.Pokemon(
//                2,
//                "Ivysaur",
//                ResourceLocation.Res(R.drawable.venusaur)
//            ),
//            "Lvl 16"
//        )
//    }
//    ScreenDevices.HuaweiP10 {
//        Surface(color = Color.Gray) {
//            Container(width = 400.dp) {
//                EvolutionTab.EvolutionChainItem(evolution = evolution)
//            }
//        }
//
//    }
//}