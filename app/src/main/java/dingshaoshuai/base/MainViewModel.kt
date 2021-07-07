package dingshaoshuai.base

import android.util.Log
import androidx.lifecycle.LifecycleOwner
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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i(TAG, "onCreate: ")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.i(TAG, "onStart: ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.i(TAG, "onResume: ")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.i(TAG, "onPause: ")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.i(TAG, "onStop: ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.i(TAG, "onDestroy: ")
    }

    companion object{
        private const val TAG = "MainViewModel"
    }

}