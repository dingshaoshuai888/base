package dingshaoshuai.base.ktx

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

inline fun <T> ViewModel.launch(
    block: () -> T?,
    checkBlock: (T?) -> Boolean,
    successBlock: (T?) -> Unit = {},
    failureBlock: (T?) -> Unit = {},
    completeBlock: (T?) -> Unit = {}
) {
    val blockValue = block()
    val checkValue = checkBlock(blockValue)
    if (checkValue) {
        successBlock(blockValue)
    } else {
        failureBlock(blockValue)
    }
    completeBlock(blockValue)
}

suspend inline fun <T> ViewModel.launchSuspend(
    crossinline blockIO: suspend () -> T?,
    crossinline checkBlock: (T?) -> Boolean,
    crossinline successBlock: suspend (T?) -> Unit,
    crossinline failureBlock: (T?) -> Unit = {},
    crossinline completeBlock: (T?) -> Unit = {}
) {
    val blockIOValue = withContext(Dispatchers.IO) {
        blockIO()
    }
    if (checkBlock(blockIOValue)) {
        successBlock(blockIOValue)
    } else {
        failureBlock(blockIOValue)
    }
    completeBlock(blockIOValue)
}