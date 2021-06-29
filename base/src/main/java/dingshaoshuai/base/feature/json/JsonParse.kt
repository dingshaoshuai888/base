package dingshaoshuai.base.feature.json

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
interface JsonParse {
    fun <T> toObject(json: String, clazz: Class<T>): T
    fun toJsonString(any: Any): String
    fun <T> toList(json: String, clazz: Class<T>): List<T>
    fun <T> toMap(json: String): Map<String, T>
    fun <T> toListMap(json: String): List<Map<String, T>>
}