package com.crskdev.compose.pokedex.ui.common.router

import android.os.Bundle

/**
 * Created by Cristian Pela on 21.01.2020.
 */
interface ScreenSerializer{

    fun onSaveInstanceState(backstack: BackstackHandler.Backstack, savedInstanceState: Bundle)

    fun onRestoreInstanceState(savedInstanceState: Bundle): BackstackHandler.Backstack

}