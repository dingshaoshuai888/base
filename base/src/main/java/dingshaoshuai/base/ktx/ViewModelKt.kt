package dingshaoshuai.base.ktx

import android.util.Log
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
    var value: T? = null
    try {
        value = withContext(Dispatchers.IO) {
            blockIO()
        }
        if (checkBlock(value)) {
            successBlock(value)
        } else {
            failureBlock(value)
        }
    } catch (e: Exception) {
        Log.e("ViewModelKt", "launchSuspend: ${e.message}")
        failureBlock(value)
    } finally {
        completeBlock(value)
    }
}