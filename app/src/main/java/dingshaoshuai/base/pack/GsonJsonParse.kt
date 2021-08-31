package dingshaoshuai.base.pack

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dingshaoshuai.base.feature.json.JsonParse

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class GsonJsonParse : JsonParse {
    override fun <T> toObject(json: String, clazz: Class<T>): T {
        return GsonBuilder().serializeNulls().create().fromJson(json, clazz)
    }

    override fun toJsonString(any: Any): String {
        return GsonBuilder().serializeNulls().create().toJson(any)
    }
}