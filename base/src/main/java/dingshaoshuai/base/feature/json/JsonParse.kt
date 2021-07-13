package dingshaoshuai.base.feature.json

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
interface JsonParse {
    fun <T> toObject(json: String, clazz: Class<T>): T
    fun toJsonString(any: Any): String
}