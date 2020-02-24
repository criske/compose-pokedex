package com.crskdev.compose.pokedex.ui.dashboard

import androidx.annotation.FloatRange
import androidx.ui.graphics.Color
import com.crskdev.compose.pokedex.ui.pokedex.ResourceLocation

/**
 * Created by Cristian Pela on 03.02.2020.
 */
data class PokemonData(
    val id: Int,
    val idStr: String,
    val name: String,
    val color: Color,
    val category: String,
    val type1: String,
    val type2: String?,
    val image: ResourceLocation,
    val description: String,
    val height: String,
    val weight: String,
    val maleProp: String,
    val femaleProp: String,
    val eggGroups: String,
    val eggCycle: String,
    val location: ResourceLocation?,
    val baseExp: String,
    val stats: List<BaseStat>,
    val evolutionChains: EvolutionChains,
    val isFavorite: Boolean
) {
    data class BaseStat(val name: String,
                        val value: String,
                        @FloatRange(from = 0.0, to = 1.0) val progress: Float)

    data class EvolutionChain(val from: Pokemon, val to: Pokemon, val level: String) {
        data class Pokemon(val id: Int, val name: String, val image: ResourceLocation)
    }

    data class EvolutionChains(val normal: List<EvolutionChain>, val mega: EvolutionChain)
}