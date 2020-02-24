package com.crskdev.compose.pokedex.ui.pokedex.filter

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.key
import androidx.compose.remember
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.res.FailedResource
import androidx.ui.res.LoadedResource
import androidx.ui.res.PendingResource
import androidx.ui.res.loadImageResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.pokedex.PokedexItemContainer
import com.crskdev.compose.pokedex.ui.utils.DrawImageContained
import com.crskdev.compose.pokedex.ui.utils.DrawImageScaled
import com.crskdev.compose.pokedex.ui.utils.imageResource
import com.crskdev.compose.pokedex.ui.utils.scale

/**
 * Created by Cristian Pela on 23.01.2020.
 */

@Composable
fun FilterGenerationScreen(onSelected: (GenerationNumber) -> Unit) {

    val pokeballImage = imageResource(R.drawable.pokeball, 80.dp)

    FilterScreenContainer {
        Column {
            Text(
                modifier = LayoutWidth.Fill + LayoutPadding(top = 24.dp, bottom = 24.dp),
                text = "Generation",
                style = (MaterialTheme.typography()).h5.merge(
                    TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                )
            )
            VerticalScroller(modifier = LayoutGravity.Center) {
                FlowRow(crossAxisSpacing = 8.dp, mainAxisAlignment = FlowMainAxisAlignment.Center) {
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation I",
                        id = R.drawable.gen1
                    ) {
                        onSelected(GenerationNumber.I)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation II",
                        id = R.drawable.gen2
                    ) {
                        onSelected(GenerationNumber.II)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation III",
                        id = R.drawable.gen3
                    ) {
                        onSelected(GenerationNumber.III)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation IV",
                        id = R.drawable.gen4
                    ) {
                        onSelected(GenerationNumber.IV)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation V",
                        id = R.drawable.gen5
                    ) {
                        onSelected(GenerationNumber.V)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation VI",
                        id = R.drawable.gen6
                    ) {
                        onSelected(GenerationNumber.VI)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation VII",
                        id = R.drawable.gen7
                    ) {
                        onSelected(GenerationNumber.VII)
                    }
                    FilterGenerationItem(
                        pokeball = pokeballImage,
                        title = "Generation VIII",
                        id = R.drawable.gen8
                    ) {
                        onSelected(GenerationNumber.VIII)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterGenerationItem(pokeball: Image, title: String, @DrawableRes id: Int, onClick: () -> Unit) {
    val genRawImage  = loadImageResource(id)
    key(id) {
        Ripple(bounded = true) {
            Clickable(onClick = onClick) {
                Surface(modifier = LayoutHeight(110.dp) + LayoutWidth(160.dp), shape = RoundedCornerShape(15.dp),
                    elevation = 3.dp) {
                    Column(arrangement = Arrangement.SpaceEvenly) {
                        Text(
                            modifier = LayoutGravity.Center,
                            text = title,
                            style = (MaterialTheme.typography()).subtitle1.merge(
                                TextStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        )
                        Container(modifier = LayoutGravity.Center) {
                            when (genRawImage.resource) {
                                is LoadedResource -> DrawImageContained(
                                    image = genRawImage.resource.resource?.scale(0.2f)!!
                                )
                                is PendingResource -> CircularProgressIndicator()
                                is FailedResource -> Text(
                                    text = "Error Loading",
                                    style = TextStyle(Color.Red)
                                )
                            }
                        }
                       // DrawImageScaled(id = id, scale = 0.8f)
                    }


//                    PokedexItemContainer(
//                        pokeballCache = pokeball
//                    ) {
//                        Text(
//                            modifier = LayoutGravity.TopCenter,
//                            text = title,
//                            style = (MaterialTheme.typography()).subtitle1.merge(
//                                TextStyle(
//                                    fontWeight = FontWeight.Bold
//                                )
//                            )
//                        )
//                        Container(modifier = LayoutGravity.BottomCenter + LayoutPadding(bottom = 16.dp)) {
//                           // DrawImageScaled(id = id, scale = 0.8f)
//                        }
//                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewGen() {
//    ScreenDevices.HuaweiP10 {
//        FilterGenerationScreen()
//    }
//}