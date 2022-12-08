package dingshaoshuai.base.mvvm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dingshaoshuai.base.BaseLifecycleObserver
import dingshaoshuai.base.ktx.launchSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author: Xiao Bo
 * @date: 11/6/2021
 */
open class BaseViewModel : ViewModel(), BaseLifecycleObserver {
    val uiChangeLiveData = UIChangeLiveData()

    inner class UIChangeLiveData {
        val onBackLiveData by lazy { CallLiveData() }

        val loadingDialogLiveData by lazy { MutableLiveData<Boolean>() }

        val startActivityLiveData by lazy { MutableLiveData<Map<String, Any>>() }
    }

    open fun initData() {}

    fun finishPage() {
        uiChangeLiveData.onBackLiveData.call()
    }

    fun loadingDialog(show: Boolean) {
        uiChangeLiveData.loadingDialogLiveData.value = show
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle? = null, flags: Int? = null) {
        val map = hashMapOf<String, Any>().apply {
            put(START_ACTIVITY_CLASS, clazz)
            bundle?.let { put(START_ACTIVITY_BUNDLE, it) }
            flags?.let { put(START_ACTIVITY_FLAGS, it) }
        }
        uiChangeLiveData.startActivityLiveData.value = map
    }

    inline fun <T> launchLoading(
        crossinline blockIO: suspend () -> T?,
        crossinline checkBlock: (T?) -> Boolean,
        crossinline successBlock: suspend (T?) -> Unit,
        crossinline failureBlock: (T?) -> Unit = {},
        displayLoadingDialog: Boolean = true,
        crossinline completeBlock: (T?) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            if (displayLoadingDialog) {
                loadingDialog(true)
            }
            launchSuspend(
                blockIO = blockIO,
                checkBlock = checkBlock,
                successBlock = successBlock,
                failureBlock = failureBlock,
                completeBlock = completeBlock
            )
            if (displayLoadingDialog) {
                loadingDialog(false)
            }
        }

    }

    companion object {
        internal const val START_ACTIVITY_CLASS = "start_activity_class"
        internal const val START_ACTIVITY_BUNDLE = "start_activity_bundle"
        internal const val START_ACTIVITY_FLAGS = "start_activity_flags"
    }
}