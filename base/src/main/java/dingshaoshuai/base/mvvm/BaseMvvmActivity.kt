package dingshaoshuai.base.mvvm

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import dingshaoshuai.base.BaseActivity

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseMvvmActivity<T : ViewDataBinding, E : BaseViewModel> : BaseActivity() {
    protected lateinit var binding: T
    protected lateinit var viewModel: E

    protected abstract fun initViewModel(): E
    protected abstract fun bindViewModel(viewModel: E)

    override fun initCustom() {
        super.initCustom()
        viewModel = initViewModel()
        bindViewModel(viewModel)
        initObserver()
    }

    override fun initContentView() {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun initObserver() {
        viewModel.uiChangeLiveData.let {
            it.onBackLiveData.observe(this, Observer {
                finish()
            })
            it.loadingDialogLiveData.observe(this, Observer { show ->
                if (show) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            })
        }
    }
}