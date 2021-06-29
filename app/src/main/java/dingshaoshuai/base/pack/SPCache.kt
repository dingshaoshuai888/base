package dingshaoshuai.base.pack

import android.content.Context
import android.content.SharedPreferences
import dingshaoshuai.base.feature.cache.Cache

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class SPCache(context: Context) : Cache {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_cache", Context.MODE_PRIVATE)

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    override fun delete(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun save(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun save(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun get(key: String, default: String): String {
        return sharedPreferences.getString(key, default) ?: default
    }

    override fun get(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    override fun get(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }
}