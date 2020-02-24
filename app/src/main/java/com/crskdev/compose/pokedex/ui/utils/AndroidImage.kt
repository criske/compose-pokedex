package com.crskdev.compose.pokedex.ui.utils

import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.remember
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.ui.core.ContextAmbient
import androidx.ui.core.DensityAmbient
import androidx.ui.core.WithDensity
import androidx.ui.core.ambientDensity
import androidx.ui.foundation.DrawImage
import androidx.ui.graphics.*
import androidx.ui.graphics.colorspace.ColorSpace
import androidx.ui.graphics.colorspace.ColorSpaces
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.Container
import androidx.ui.res.vectorResource
import androidx.ui.unit.*
import java.io.InputStream
import kotlin.math.roundToInt


/**
 * Created by Cristian Pela on 02.01.2020.
 */
class AndroidImage(val bitmap: Bitmap) : Image {

    /**
     * @see Image.width
     */
    override val width: Int
        get() = bitmap.width

    /**
     * @see Image.height
     */
    override val height: Int
        get() = bitmap.height

    override val config: ImageConfig
        get() = bitmap.config.toImageConfig()

    /**
     * @see Image.colorSpace
     */
    override val colorSpace: ColorSpace
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bitmap.colorSpace?.toComposeColorSpace() ?: ColorSpaces.Srgb
        } else {
            ColorSpaces.Srgb
        }

    /**
     * @see Image.hasAlpha
     */
    override val hasAlpha: Boolean
        get() = bitmap.hasAlpha()

    /**
     * @see Image.nativeImage
     */
    override val nativeImage: NativeImage
        get() = bitmap

    /**
     * @see
     */
    override fun prepareToDraw() {
        bitmap.prepareToDraw()
    }
}

internal fun ImageConfig.toBitmapConfig(): Bitmap.Config {
    // Cannot utilize when statements with enums that may have different sets of supported
    // values between the compiled SDK and the platform version of the device.
    // As a workaround use if/else statements
    // See https://youtrack.jetbrains.com/issue/KT-30473 for details
    return if (this == ImageConfig.Argb8888) {
        Bitmap.Config.ARGB_8888
    } else if (this == ImageConfig.Alpha8) {
        Bitmap.Config.ALPHA_8
    } else if (this == ImageConfig.Rgb565) {
        Bitmap.Config.RGB_565
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == ImageConfig.F16) {
        Bitmap.Config.RGBA_F16
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == ImageConfig.Gpu) {
        Bitmap.Config.HARDWARE
    } else {
        Bitmap.Config.ARGB_8888
    }
}

private fun Bitmap.Config.toImageConfig(): ImageConfig {
    // Cannot utilize when statements with enums that may have different sets of supported
    // values between the compiled SDK and the platform version of the device.
    // As a workaround use if/else statements
    // See https://youtrack.jetbrains.com/issue/KT-30473 for details
    @Suppress("DEPRECATION")
    return if (this == Bitmap.Config.ALPHA_8) {
        ImageConfig.Alpha8
    } else if (this == Bitmap.Config.RGB_565) {
        ImageConfig.Rgb565
    } else if (this == Bitmap.Config.ARGB_4444) {
        ImageConfig.Argb8888 // Always upgrade to Argb_8888
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == Bitmap.Config.RGBA_F16) {
        ImageConfig.F16
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && this == Bitmap.Config.HARDWARE) {
        ImageConfig.Gpu
    } else {
        ImageConfig.Argb8888
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun ColorSpace.toFrameworkColorSpace(): android.graphics.ColorSpace {
    val frameworkNamedSpace = when (this) {
        ColorSpaces.Srgb -> android.graphics.ColorSpace.Named.SRGB
        ColorSpaces.Aces -> android.graphics.ColorSpace.Named.ACES
        ColorSpaces.Acescg -> android.graphics.ColorSpace.Named.ACESCG
        ColorSpaces.AdobeRgb -> android.graphics.ColorSpace.Named.ADOBE_RGB
        ColorSpaces.Bt2020 -> android.graphics.ColorSpace.Named.BT2020
        ColorSpaces.Bt709 -> android.graphics.ColorSpace.Named.BT709
        ColorSpaces.CieLab -> android.graphics.ColorSpace.Named.CIE_LAB
        ColorSpaces.CieXyz -> android.graphics.ColorSpace.Named.CIE_XYZ
        ColorSpaces.DciP3 -> android.graphics.ColorSpace.Named.DCI_P3
        ColorSpaces.DisplayP3 -> android.graphics.ColorSpace.Named.DISPLAY_P3
        ColorSpaces.ExtendedSrgb -> android.graphics.ColorSpace.Named.EXTENDED_SRGB
        ColorSpaces.LinearExtendedSrgb ->
            android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB
        ColorSpaces.LinearSrgb -> android.graphics.ColorSpace.Named.LINEAR_SRGB
        ColorSpaces.Ntsc1953 -> android.graphics.ColorSpace.Named.NTSC_1953
        ColorSpaces.ProPhotoRgb -> android.graphics.ColorSpace.Named.PRO_PHOTO_RGB
        ColorSpaces.SmpteC -> android.graphics.ColorSpace.Named.SMPTE_C
        else -> android.graphics.ColorSpace.Named.SRGB
    }
    return android.graphics.ColorSpace.get(frameworkNamedSpace)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun android.graphics.ColorSpace.toComposeColorSpace(): ColorSpace {
    return when (this) {
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB)
        -> ColorSpaces.Srgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ACES)
        -> ColorSpaces.Aces
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ACESCG)
        -> ColorSpaces.Acescg
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.ADOBE_RGB)
        -> ColorSpaces.AdobeRgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.BT2020)
        -> ColorSpaces.Bt2020
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.BT709)
        -> ColorSpaces.Bt709
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.CIE_LAB)
        -> ColorSpaces.CieLab
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.CIE_XYZ)
        -> ColorSpaces.CieXyz
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.DCI_P3)
        -> ColorSpaces.DciP3
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.DISPLAY_P3)
        -> ColorSpaces.DisplayP3
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.EXTENDED_SRGB)
        -> ColorSpaces.ExtendedSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.LINEAR_EXTENDED_SRGB)
        -> ColorSpaces.LinearExtendedSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.LINEAR_SRGB)
        -> ColorSpaces.LinearSrgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.NTSC_1953)
        -> ColorSpaces.Ntsc1953
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.PRO_PHOTO_RGB)
        -> ColorSpaces.ProPhotoRgb
        android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SMPTE_C)
        -> ColorSpaces.SmpteC
        else -> ColorSpaces.Srgb
    }
}


fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}


fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap =
    decodeSampledBitmap(reqWidth, reqHeight) {
        decodeResource(res, resId, it)
    }

fun decodeSampledBitmapFromResource(res: Resources, resId: Int, scale: Float): Bitmap =
    decodeSampledBitmap(scale) {
        decodeResource(res, resId, it)
    }


fun decodeSampledBitmapFromAssets(assets: AssetManager, fileName: String, reqWidth: Int, reqHeight: Int): Bitmap =
    decodeSampledBitmap(reqWidth, reqHeight) {
        try {
            val stream = assets.open(fileName)
            decodeStream(stream, it).apply {
                stream.close()
            }
        } catch (ex: Exception) {
            throw  ex
        }

    }


object BitmapFactoryScope {
    fun decodeResource(res: Resources, resId: Int, options: BitmapFactory.Options): Bitmap =
        BitmapFactory.decodeResource(res, resId, options)

    fun decodeStream(stream: InputStream, options: BitmapFactory.Options): Bitmap =
        BitmapFactory.decodeStream(stream, null, options)
            ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
}


inline fun decodeSampledBitmap(
    scale: Float,
    factory: BitmapFactoryScope.(BitmapFactory.Options) -> Bitmap
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactoryScope.also { scope ->
            scope.factory(this)
        }

        val reqWidth = (outWidth * scale).roundToInt()
        val reqHeight = (outHeight * scale).roundToInt()

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false
        val loadedBitmap = BitmapFactoryScope.let { scope ->
            scope.factory(this)
        }
        Bitmap.createScaledBitmap(loadedBitmap, reqWidth, reqHeight, true).apply {
            loadedBitmap.recycle()
        }
    }
}

inline fun decodeSampledBitmap(
    reqWidth: Int,
    reqHeight: Int,
    factory: BitmapFactoryScope.(BitmapFactory.Options) -> Bitmap
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactoryScope.also { scope ->
            scope.factory(this)
        }
        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false
        val loadedBitmap = BitmapFactoryScope.let { scope ->
            scope.factory(this)
        }
        Bitmap.createScaledBitmap(loadedBitmap, reqWidth, reqHeight, true).apply {
            loadedBitmap.recycle()
        }
    }
}

@Composable
fun imageResource(@DrawableRes id: Int, width: Dp, height: Dp): Image {
    val context = ambient(ContextAmbient)
    val density = ambient(DensityAmbient)
    return remember(id, width, height) {
        DensityScope(density).run {
            val scaledBitmap = decodeSampledBitmapFromResource(
                context.resources,
                id,
                width.toIntPx().value,
                height.toIntPx().value
            )
            AndroidImage(scaledBitmap)
        }
    }
}

@Composable
fun imageResourceScaled(@DrawableRes id: Int, scale: Float): Image {
    val context = ambient(ContextAmbient)
    return remember(id, scale) {
        val scaledBitmap = decodeSampledBitmapFromResource(context.resources, id, scale)
        AndroidImage(scaledBitmap)
    }
}


fun Image.scale(scale: Float): Image {
    val reqWidth = (width * scale).roundToInt()
    val reqHeight = (height * scale).roundToInt()
    return AndroidImage(Bitmap.createScaledBitmap(nativeImage, reqWidth, reqHeight, true))
}

@Composable
fun imageResource(@DrawableRes id: Int, size: Dp): Image = imageResource(id, size, size)

@Composable
fun imageAsset(fileName: String, width: Dp, height: Dp): Image {
    val context = ambient(ContextAmbient)
    val density = ambient(DensityAmbient)
    return remember(fileName, width, height) {
        DensityScope(density).run {
            val scaledBitmap = decodeSampledBitmapFromAssets(
                context.assets,
                fileName,
                width.toIntPx().value,
                height.toIntPx().value
            )
            AndroidImage(scaledBitmap)
        }
    }
}

@Composable
fun VectorImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
    val vector = vectorResource(id)
    Container(width = vector.defaultWidth, height = vector.defaultHeight) {
        DrawVector(vector, tint)
    }

}

@Composable
fun VectorImage(@DrawableRes id: Int, tint: Color = Color.Transparent, size: Pair<Dp, Dp> = 24.dp to 24.dp) {
    val vector = imageVectorResource(id, tint, size)
    Container(width = size.first, height = size.second) {
        DrawImage(image = vector)
    }
}

@Composable
fun imageVectorResource(@DrawableRes id: Int, tint: Color = Color.Black, size: Pair<Dp, Dp> = 24.dp to 24.dp): Image {
    val key = "$id#${tint.value}#${size.first.value}-${size.second.value}"
    val density = ambientDensity()
    val context = ambient(ContextAmbient)
    return remember<Image>(key) {
        val (wPx, hPx) = DensityScope(density).run {
            size.first.toIntPx().value to size.second.toIntPx().value
        }
        val bitmap = AppCompatResources.getDrawable(context, id)
            ?.apply { DrawableCompat.setTint(this, tint.toArgb()) }
            ?.toBitmap(wPx, hPx)
            ?: throw Resources.NotFoundException("Vector resource not found")
        AndroidImage(bitmap)
    }
}

@Composable
fun DrawImageFromAssets(fileName: String, width: Dp, height: Dp, tint: Color? = null) {
    Container(width = width, height = height) {
        DrawImage(image = imageAsset(fileName, width, height), tint = tint)
    }
}

@Composable
fun DrawImage(@DrawableRes id: Int, width: Dp, height: Dp, tint: Color? = null) {
    val image = imageResource(id, width, height)
    WithDensity {
        Container(
            width = image.nativeImage.width.px.toDp(),
            height = image.nativeImage.height.px.toDp()
        ) {
            DrawImage(image = image, tint = tint)
        }
    }

}

@Composable
fun DrawImage(image: Image, width: Dp, height: Dp, tint: Color? = null) {
    val density = ambientDensity()
    val scaled = remember{
        withDensity(density){
            AndroidImage(Bitmap.createScaledBitmap(image.nativeImage, width.toIntPx().value, height.toIntPx().value, true))
        }
    }
    Container(width = width, height = height) {
        WithDensity {
            DrawImage(image = scaled, tint = tint)
        }

    }
}

@Composable
fun DrawImageContained(image: Image, tint: Color? = null) {
    WithDensity {
        Container(
            width = image.width.px.toDp(),
            height = image.height.px.toDp()
        ) {
            DrawImage(image = image, tint = tint)
        }
    }
}

@Composable
fun DrawImageScaled(@DrawableRes id: Int, scale: Float, tint: Color? = null) {
    val image = imageResourceScaled(id, scale)
    WithDensity {
        Container(
            width = image.width.px.toDp(),
            height = image.height.px.toDp()
        ) {
            DrawImage(image = image, tint = tint)
        }
    }
}

@Composable
fun DrawImage(@DrawableRes id: Int, size: Dp, tint: Color? = null) {
    DrawImage(id, size, size, tint)
}

@Composable
fun DrawImage(image: Image, size: Dp, tint: Color? = null) {
    DrawImage(image, size, size, tint)
}

