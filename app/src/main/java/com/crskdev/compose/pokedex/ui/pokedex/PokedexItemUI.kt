package com.crskdev.compose.pokedex.ui.pokedex

import androidx.annotation.ColorRes
import java.io.File
import java.net.URL

/**
 * Created by Cristian Pela on 04.01.2020.
 */
data class PokedexItemUI(val id: Int,
                         val order: String,
                         val name: String,
                         val type1: String,
                         val type2: String = "",
                         @ColorRes val colorId: Int,
                         val image: ResourceLocation)

sealed class ResourceLocation() {
    class Remote(val url: String) : ResourceLocation()
    class Local(val filePath: String) : ResourceLocation()
    class Asset(val fileName: String) : ResourceLocation()
    class Res(val id: Int) : ResourceLocation()
}