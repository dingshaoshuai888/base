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

    override fun <T> toList(json: String, clazz: Class<T>): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return GsonBuilder().serializeNulls().create().fromJson<List<T>>(json, type)
    }

    override fun <T> toMap(json: String): Map<String, T> {
        val type = object : TypeToken<Map<String, T>>() {}.type
        return GsonBuilder().serializeNulls().create().fromJson(json, type)
    }

    override fun <T> toListMap(json: String): List<Map<String, T>> {
        val type = object : TypeToken<List<Map<String, T>>>() {}.type
        return GsonBuilder().serializeNulls().create().fromJson(json, type)
    }
}