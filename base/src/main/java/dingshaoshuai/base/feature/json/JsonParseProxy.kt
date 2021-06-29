package dingshaoshuai.base.feature.json

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class JsonParseProxy private constructor(jsonParse: JsonParse) : JsonParse by jsonParse {

    companion object {
        lateinit var instance: JsonParseProxy
            private set

        fun init(jsonParse: JsonParse) {
            if (!::instance.isInitialized) {
                synchronized(JsonParseProxy::class.java) {
                    if (!::instance.isInitialized) {
                        instance = JsonParseProxy(jsonParse)
                    }
                }
            }
        }
    }
}