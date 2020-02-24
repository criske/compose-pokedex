package com.crskdev.compose.pokedex.ui.common

import androidx.compose.*
import androidx.ui.core.FocusManagerAmbient
import androidx.ui.core.TextField
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.input.ImeAction
import androidx.ui.layout.*
import androidx.ui.res.stringResource
import androidx.ui.text.TextStyle
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.utils.VectorImage
import com.crskdev.compose.pokedex.ui.utils.nativeColor
import java.util.*

/**
 * Created by Cristian Pela on 08.01.2020.
 */
@Composable
fun PokemonSearchField(requestFocus: Boolean = false, onSearch: (String) -> Unit = {}) {
    var searchState by state { "" }
    val focusManager = ambient(FocusManagerAmbient)
    val focusId = remember { "PokemonSearchField-${UUID.randomUUID()}" }
    Container(height = 45.dp) {
        DrawShape(shape = RoundedCornerShape(30.dp), color = nativeColor(R.color.lightGrey))
        Padding(padding = EdgeInsets(left = 16.dp, right = 16.dp)) {
            FlexRow {
                inflexible {
                    VectorImage(
                        id = R.drawable.ic_baseline_search_24,
                        tint = nativeColor(R.color.black),
                        size = 24.dp to 24.dp
                    )
                }
                inflexible {
                    Spacer(modifier = LayoutHeight(16.dp))
                }
                flexible(1f) {
                    HintTextField(
                        hint = stringResource(R.string.search_hint),
                        hintStyle = TextStyle(color = (nativeColor(R.color.grey)).copy(alpha = 0.7f))
                    ) { isEmpty ->
                        @Composable() {
                            onActive {
                                isEmpty.value = searchState.isEmpty()
                            }
                            onCommit(requestFocus) {
                                if (requestFocus)
                                    focusManager.requestFocusById(focusId)
                            }
                            TextField(
                                modifier = LayoutWidth.Fill,
                                focusIdentifier = focusId,
                                textStyle = TextStyle(fontSize = 16.sp),
                                value = searchState,
                                onValueChange = {
                                    searchState = it
                                    isEmpty.value = it.isEmpty()
                                },
                                imeAction = ImeAction.Search,
                                onImeActionPerformed = {
                                    if (it == ImeAction.Search) {
                                        onSearch(searchState)
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}