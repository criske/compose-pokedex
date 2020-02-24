package com.crskdev.compose.pokedex.ui.pokedex.filter

import androidx.compose.Composable
import androidx.ui.layout.EdgeInsets
import androidx.ui.layout.Padding
import androidx.ui.layout.Wrap
import androidx.ui.unit.dp
import com.crskdev.compose.pokedex.ui.common.PokemonSearchField

/**
 * Created by Cristian Pela on 18.01.2020.
 */
@Composable
fun FilterSearchScreen(requestFocus: Boolean = false, onSearch: (String) -> Unit) {
    Wrap {
        FilterScreenContainer {
            Padding(
                padding = EdgeInsets(
                    left = 24.dp,
                    right = 24.dp,
                    top = 36.dp,
                    bottom = 36.dp
                )
            ) {
                PokemonSearchField(requestFocus = requestFocus, onSearch = onSearch)
            }
        }
    }
}