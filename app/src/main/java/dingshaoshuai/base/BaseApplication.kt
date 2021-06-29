package dingshaoshuai.base

import android.app.Application
import dingshaoshuai.base.feature.cache.CacheProxy
import dingshaoshuai.base.feature.image.ImageLoaderProxy
import dingshaoshuai.base.feature.json.JsonParseProxy
import dingshaoshuai.base.pack.GlideImageLoader
import dingshaoshuai.base.pack.GsonJsonParse
import dingshaoshuai.base.pack.SPCache

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageLoaderProxy.init(GlideImageLoader())
        JsonParseProxy.init(GsonJsonParse())
        CacheProxy.init(SPCache(this))
    }
}