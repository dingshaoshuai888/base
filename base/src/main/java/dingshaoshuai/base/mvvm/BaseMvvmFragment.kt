package dingshaoshuai.base.mvvm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dingshaoshuai.base.BaseFragment

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseMvvmFragment<V : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    protected abstract fun initViewModel(): VM
    protected abstract fun bindViewModel(viewModel: VM)

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
            it.loadingDialogLiveData.observe(this) { show ->
                mActivity?.let { baseActivity ->
                    if (show) {
                        baseActivity.showLoadingDialog()
                    } else {
                        baseActivity.dismissLoadingDialog()
                    }
                }
            }
            it.startActivityLiveData.observe(this) { map ->
                val activity = mActivity ?: return@observe
                val clazz = map[BaseViewModel.START_ACTIVITY_CLASS]
                val bundle = map[BaseViewModel.START_ACTIVITY_BUNDLE]
                val flags = map[BaseViewModel.START_ACTIVITY_FLAGS]
                val intent = Intent().apply {
                    setClass(activity, clazz as Class<*>)
                    bundle?.let { bundle -> putExtras(bundle as Bundle) }
                    flags?.let { flags -> this.flags = flags as Int }
                }
                activity.startActivity(intent)
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