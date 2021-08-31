package dingshaoshuai.base.feature.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
interface ImageLoader {
    fun load(imageView: ImageView, @DrawableRes resourceId: Int)
    fun load(imageView: ImageView, url: String, @DrawableRes defaultResourceId: Int)
    fun load(imageView: ImageView, url: String, defaultDrawable: Drawable)
    fun load(imageView: ImageView, bitmap: Bitmap, defaultDrawable: Drawable)
    fun load(imageView: ImageView, uri: Uri, defaultDrawable: Drawable)
}