package com.crskdev.compose.pokedex.ui.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.imageFromResource
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.crskdev.compose.pokedex.R
import com.crskdev.compose.pokedex.ui.pokedex.ResourceLocation
import com.crskdev.compose.pokedex.ui.utils.AndroidImage
import com.crskdev.compose.pokedex.ui.utils.DrawImage
import com.crskdev.compose.pokedex.ui.utils.DrawImageFromAssets

/**
 * Created by Cristian Pela on 03.01.2020.
 */
@Composable
fun HintTextField(hint: String, hintStyle: TextStyle, textField: (MutableState<Boolean>) -> @Composable() () -> Unit) {
    val isEmpty = state { false }
    val hintText = @Composable {
        Text(
            text = hint,
            style = hintStyle,
            softWrap = false,
            maxLines = 1,
            modifier = LayoutWidth.Fill,
            overflow = TextOverflow.Ellipsis
        )
    }
    if (!isEmpty.value) {
        textField(isEmpty)()
    } else {
        Layout(textField(isEmpty), hintText) { measurable, constraints ->
            val inputfieldPlaceable = measurable[0].measure(constraints)
            val hintTextPlaceable = measurable[1].measure(constraints)
            layout(inputfieldPlaceable.width, inputfieldPlaceable.height) {
                inputfieldPlaceable.place(0.ipx, 0.ipx)
                hintTextPlaceable.place(0.ipx, 0.ipx)
            }
        }
    }
}


@Composable
fun LoadImage(location: ResourceLocation, width: Dp, height: Dp, tint: Color? = null) {
    when (location) {
        is ResourceLocation.Remote -> GlideImage(
            path = location.url,
            width = width,
            height = height,
            tint = tint
        )
        is ResourceLocation.Local -> TODO()
        is ResourceLocation.Asset -> DrawImageFromAssets(location.fileName, width, height, tint)
        is ResourceLocation.Res -> DrawImage(
            id = location.id,
            width = width,
            height = height,
            tint = tint
        )
    }
}

@Composable
fun LoadImage(location: ResourceLocation, size: Dp, tint: Color? = null) {
    LoadImage(
        location = location,
        width = size,
        height = size,
        tint = tint
    )
}


@Composable
fun GlideImage(path: String,
               @DrawableRes placeholder: Int? = null,
               width: Dp,
               height: Dp,
               tint: Color? = null,
               builder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }) {
    val context = ambient(ContextAmbient)
    val (bitmap, setBitmap) = state {
        placeholder?.let { imageFromResource(context.resources, placeholder).nativeImage }
    }
    onCommit(path) {
        withDensity(Density(context)) {
            val requestManager = Glide.with(context)
            val target: Target<Bitmap> = requestManager
                .asBitmap()
                .load(path)
                .let(builder)
                .override(width.toIntPx().value, height.toIntPx().value)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        throw e ?: Exception("Unknown Glide Exception")
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean =
                        false
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) = Unit
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) =
                        setBitmap(resource)
                })
            onDispose {
                requestManager.clear(target)
            }
        }
    }
    bitmap?.also {
        DrawImage(
            image = AndroidImage(it),
            width = width,
            height = height,
            tint = tint
        )
    }
}

@Composable
fun PokeballImage(size: Dp, tint: Color = Color.White.copy(alpha = 0.15f)) {
    DrawImage(id = R.drawable.pokeball, size = size, tint = tint)
}

@Composable
fun PokeballBgContainer(
    modifier: Modifier = Modifier.None,
    color: Color = (MaterialTheme.colors()).surface, children: @Composable() StackScope.() -> Unit) {
    val pokeballTint = color.localThemeType.onSurface().copy(alpha = 0.05f)
    val allChildren = @Composable() {
        PokeballImage(size = 250.dp, tint = pokeballTint)
        Stack(modifier = LayoutSize.Fill) { this.children() }
    }
    Container(modifier = modifier, alignment = Alignment.TopLeft) {
        DrawShape(shape = RectangleShape, color = color)
        Layout(modifier = LayoutSize.Fill, children = allChildren) { measurables, constraints ->
            val placeables = measurables.map {
                it.measure(constraints.copy(minWidth = 0.ipx, minHeight = 0.ipx))
            }
            layout(constraints.maxWidth, constraints.maxHeight) {
                val pokeballPlaceable = placeables.first()
                pokeballPlaceable.place(
                    Alignment.TopRight.align(pokeballPlaceable.size)
                            + IntPxPosition(5.dp.toIntPx(), -50.dp.toIntPx())
                )
                placeables.last().place(0.ipx, 0.ipx)
            }
        }
    }
}