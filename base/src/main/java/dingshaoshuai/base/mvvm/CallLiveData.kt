package dingshaoshuai.base.mvvm

import androidx.lifecycle.MutableLiveData

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
class CallLiveData : MutableLiveData<Void>() {

    override fun setValue(value: Void?) {
        super.setValue(null)
    }

    override fun postValue(value: Void?) {
        super.postValue(null)
    }

    fun call() {
        value = null
    }

    fun postCall() {
        postValue(null)
    }
}