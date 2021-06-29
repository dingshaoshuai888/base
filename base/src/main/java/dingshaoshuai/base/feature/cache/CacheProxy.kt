package dingshaoshuai.base.feature.cache

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class CacheProxy private constructor(cache: Cache) : Cache by cache {

    companion object {
        lateinit var instance: CacheProxy
            private set

        fun init(cache: Cache) {
            if (!::instance.isInitialized) {
                synchronized(CacheProxy::class.java) {
                    if (!::instance.isInitialized) {
                        instance = CacheProxy(cache)
                    }
                }
            }
        }
    }
}