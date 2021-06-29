package dingshaoshuai.base.feature.cache

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
interface Cache {
    fun clear()
    fun delete(key: String)

    fun save(key: String, value: String)
    fun get(key: String, default: String): String

    fun save(key: String, value: Boolean)
    fun get(key: String, default: Boolean): Boolean

    fun save(key: String, value: Int)
    fun get(key: String, default: Int): Int
}