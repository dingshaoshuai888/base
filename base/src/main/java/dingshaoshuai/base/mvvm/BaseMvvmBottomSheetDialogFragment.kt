package dingshaoshuai.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dingshaoshuai.base.BaseBottomSheetDialogFragment

abstract class BaseMvvmBottomSheetDialogFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseBottomSheetDialogFragment() {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    protected abstract fun initViewModel(): VM
    protected abstract fun bindViewModel(viewModel: VM)
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
        // 防止应用被后台杀死提前 finish 导致 viewModel 还没有初始化
        if (::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel)
        }
    }
}