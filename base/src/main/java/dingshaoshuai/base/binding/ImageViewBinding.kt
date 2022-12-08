package dingshaoshuai.base.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import dingshaoshuai.base.feature.image.ImageLoaderProxy

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
@BindingAdapter("url", "defaultResourceId", requireAll = true)
fun bindingImage(imageView: ImageView, url: String, @DrawableRes defaultResourceId: Int) {
    ImageLoaderProxy.instance.load(imageView, url, defaultResourceId)
}

@BindingAdapter("url", "defaultDrawable", requireAll = true)
fun bindingImage(imageView: ImageView, url: String, drawable: Drawable) {
    ImageLoaderProxy.instance.load(imageView, url, drawable)
}
