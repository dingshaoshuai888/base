package dingshaoshuai.base.mvvm

import androidx.databinding.ObservableField

/**
 * @author: Xiao Bo
 * @date: 17/6/2021
 */
class ObservableString(value: String? = null) : ObservableField<String>(value) {

    override fun get(): String {
        return super.get() ?: ""
    }
}