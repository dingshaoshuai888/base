package dingshaoshuai.base.feature.image

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class ImageLoaderProxy private constructor(imageLoader: ImageLoader) : ImageLoader by imageLoader {

    companion object {
        lateinit var instance: ImageLoaderProxy
            private set

        fun init(imageLoader: ImageLoader) {
            if (!::instance.isInitialized) {
                synchronized(ImageLoaderProxy::class.java) {
                    if (!::instance.isInitialized) {
                        instance = ImageLoaderProxy(imageLoader)
                    }
                }
            }
        }
    }
}