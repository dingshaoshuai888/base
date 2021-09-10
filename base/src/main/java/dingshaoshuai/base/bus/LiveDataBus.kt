package dingshaoshuai.base.bus

import androidx.lifecycle.MutableLiveData

/**
 * @author: Xiao Bo
 * @date: 6/9/2021
 */
object LiveDataBus {

    private val map = HashMap<String, MutableLiveData<Any>>()

    fun <T> with(key: String, clazz: Class<T>): MutableLiveData<T> {
        val mapKey = "$key${clazz.name}"
        if (!map.containsKey(mapKey)) {
            map[mapKey] = MutableLiveData()
        }
        return map[mapKey] as MutableLiveData<T>
    }
}