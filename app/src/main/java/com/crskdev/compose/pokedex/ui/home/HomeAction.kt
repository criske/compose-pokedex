package com.crskdev.compose.pokedex.ui.home

import androidx.annotation.StringRes
import com.crskdev.compose.pokedex.R

/**
 * Created by Cristian Pela on 03.01.2020.
 */
sealed class HomeAction {
    sealed class Category(@StringRes val title: Int) : HomeAction() {
        object Pokedex : Category(R.string.pokedex)
        object Moves : Category(R.string.moves)
        object Abilities : Category(R.string.abilities)
        object Items : Category(R.string.items)
        object Locations : Category(R.string.locations)
        object TypeCharts : Category(R.string.type_charts)
    }

    class SearchItem(val value: String) : HomeAction()
}