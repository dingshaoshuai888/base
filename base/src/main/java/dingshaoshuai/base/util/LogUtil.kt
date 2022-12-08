package dingshaoshuai.base.util

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import dingshaoshuai.base.BaseLifecycleObserver

object LogUtil : BaseLifecycleObserver {

    internal var isDebug = false

    fun i(tag: String, msg: String) {
        if (!isDebug) return
        Log.i(tag, msg)
    }

    private fun print(event: String, page: Any) {
        i("pageChange", "$event - ${page.javaClass.name}")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        print("onCreate", owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        print("onDestroy", owner)
    }
}