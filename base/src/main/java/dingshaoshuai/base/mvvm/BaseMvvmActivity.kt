package dingshaoshuai.base.mvvm

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dingshaoshuai.base.BaseActivity

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseMvvmActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    protected abstract fun initViewModel(): VM
    protected abstract fun bindViewModel(viewModel: VM)

    override fun initCustom() {
        super.initCustom()
        initObserver()
    }

    override fun initContentView() {
        viewModel = initViewModel()
        lifecycle.addObserver(viewModel)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        bindViewModel(viewModel)
    }

    protected open fun initObserver() {
        viewModel.uiChangeLiveData.let {
            it.onBackLiveData.observe(this) {
                finish()
            }
            it.loadingDialogLiveData.observe(this) { show ->
                if (show) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            }
            it.startActivityLiveData.observe(this) { map ->
                val clazz = map[BaseViewModel.START_ACTIVITY_CLASS]
                val bundle = map[BaseViewModel.START_ACTIVITY_BUNDLE]
                val flags = map[BaseViewModel.START_ACTIVITY_FLAGS]
                val intent = Intent().apply {
                    setClass(this@BaseMvvmActivity, clazz as Class<*>)
                    bundle?.let { bundle -> putExtras(bundle as Bundle) }
                    flags?.let { flags -> this.flags = flags as Int }
                }
                startActivity(intent)
            }
        }
    }

    override fun initData() {
        super.initData()
        viewModel.initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 防止应用被后台杀死提前 finish 导致 viewModel 还没有初始化
        if (::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel)
        }
    }
}