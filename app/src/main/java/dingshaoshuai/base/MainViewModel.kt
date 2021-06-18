package dingshaoshuai.base

import androidx.lifecycle.MutableLiveData
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import dingshaoshuai.base.mvvm.ObservableString

/**
 * @author: Xiao Bo
 * @date: 18/6/2021
 */
class MainViewModel : BaseViewModel() {

    val title = ObservableString("初始化")

    val liveData = MutableLiveData<String>()
    val callLiveData = CallLiveData()

    fun change(){
        title.set("改变了")
        liveData.value = "哈哈"
        callLiveData.call()
    }

}