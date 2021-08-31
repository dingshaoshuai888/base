package dingshaoshuai.base.pack

import android.util.Log
import dingshaoshuai.base.bean.Study
import dingshaoshuai.base.feature.cache.CacheProxy
import dingshaoshuai.base.feature.json.JsonParseProxy
import dingshaoshuai.base.mvvm.BaseViewModel

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class PackViewModel : BaseViewModel() {

    fun testJson() {
        val study = Study("小明", "20", "男")

        val jsonString = JsonParseProxy.instance.toJsonString(study)
        Log.i(TAG, "jsonString: $jsonString")

        val toObject = JsonParseProxy.instance.toObject(
            "{\"age\":\"20\",\"name\":\"小明\",\"sex\":\"男\"}",
            Study::class.java
        )
        Log.i(TAG, "toObject: $toObject")
    }

    companion object {
        private const val TAG = "PackViewModel"
    }

    fun testCache() {
        Log.i(TAG, "testCache3: ${CacheProxy.instance.save("key1", "value1")}")
        Log.i(TAG, "testCache5: ${CacheProxy.instance.save("key2", "value2")}")
        Log.i(TAG, "testCache5: ${CacheProxy.instance.save("key3", "value3")}")
        Log.i(TAG, "testCache5: ${CacheProxy.instance.save("key4", true)}")
        CacheProxy.instance.clear()
    }
}