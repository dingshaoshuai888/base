package dingshaoshuai.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import dingshaoshuai.base.BaseFragment

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseMvvmFragment<T : ViewDataBinding, E : BaseViewModel> : BaseFragment() {
    protected lateinit var binding: T
    protected lateinit var viewModel: E

    protected abstract fun initViewModel(): E
    protected abstract fun bindViewModel(viewModel: E)

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

    protected open fun initObserver() {
        viewModel.uiChangeLiveData.let {
            it.loadingDialogLiveData.observe(this, Observer { show ->
                mActivity?.let { baseActivity ->
                    if (show) {
                        baseActivity.showLoadingDialog()
                    } else {
                        baseActivity.dismissLoadingDialog()
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

}