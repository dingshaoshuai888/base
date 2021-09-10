package dingshaoshuai.base.ktx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author: Xiao Bo
 * @date: 20/8/2021
 */
inline fun <reified T : ViewModel> ViewModelStoreOwner.defaultViewModel(): T {
    return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
        .get(T::class.java)
}