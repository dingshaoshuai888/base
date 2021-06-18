package dingshaoshuai.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
open class BaseViewModel : ViewModel() {
    val uiChangeLiveData = UIChangeLiveData()

    inner class UIChangeLiveData {
        val onBackLiveData by lazy {
            CallLiveData()
        }

        val loadingDialogLiveData by lazy {
            MutableLiveData<Boolean>()
        }

    }

    fun finishPage() {
        uiChangeLiveData.onBackLiveData.call()
    }

    fun loadingDialog(show: Boolean) {
        uiChangeLiveData.loadingDialogLiveData.value = show
    }
}