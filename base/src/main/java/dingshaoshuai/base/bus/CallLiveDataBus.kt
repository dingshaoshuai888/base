package dingshaoshuai.base.bus

import dingshaoshuai.base.mvvm.CallLiveData

/**
 * @author: Xiao Bo
 * @date: 6/9/2021
 */
object CallLiveDataBus {

    private val map = HashMap<String, CallLiveData>()

    fun with(key: String): CallLiveData {
        if (!map.containsKey(key)) {
            map[key] = CallLiveData()
        }
        return map[key]!!
    }
}