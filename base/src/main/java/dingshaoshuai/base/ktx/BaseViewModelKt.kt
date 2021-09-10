package dingshaoshuai.base.ktx

import androidx.lifecycle.viewModelScope
import dingshaoshuai.base.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun <T> BaseViewModel.launchLoading(
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