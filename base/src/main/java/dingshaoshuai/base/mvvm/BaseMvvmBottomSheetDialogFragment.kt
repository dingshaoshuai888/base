package dingshaoshuai.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dingshaoshuai.base.BaseBottomSheetDialogFragment

abstract class BaseMvvmBottomSheetDialogFragment<T : ViewDataBinding, E : BaseViewModel> : BaseBottomSheetDialogFragment() {
    protected lateinit var binding: T
    protected lateinit var viewModel: E

    protected abstract fun initViewModel(): E
    protected abstract fun bindViewModel(viewModel: E)
    protected open fun initObserver() {}

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = initViewModel()
        lifecycle.addObserver(viewModel)
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        bindViewModel(viewModel)
        return binding.root
    }

    override fun initCustom(view: View) {
        super.initCustom(view)
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}