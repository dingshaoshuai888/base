package dingshaoshuai.base.pack

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dingshaoshuai.base.feature.image.ImageLoader

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class GlideImageLoader : ImageLoader {
    override fun load(imageView: ImageView, resourceId: Int) {
        Glide.with(imageView).load(resourceId).into(imageView)
    }

    override fun load(imageView: ImageView, url: String, defaultResourceId: Int) {
        val requestOptions =
            RequestOptions().error(defaultResourceId).placeholder(defaultResourceId)
        Glide.with(imageView).load(url).apply(requestOptions).into(imageView)
    }

    override fun load(imageView: ImageView, bitmap: Bitmap) {
        Glide.with(imageView).load(bitmap).into(imageView)
    }

    override fun load(imageView: ImageView, uri: Uri) {
        Glide.with(imageView).load(uri).into(imageView)
    }
}