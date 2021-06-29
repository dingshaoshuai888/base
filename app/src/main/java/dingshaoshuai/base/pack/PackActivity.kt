package dingshaoshuai.base.pack

import androidx.lifecycle.ViewModelProvider
import dingshaoshuai.base.R
import dingshaoshuai.base.databinding.ActivityPackBinding
import dingshaoshuai.base.mvvm.BaseMvvmActivity

/**
 * @author: Xiao Bo
 * @date: 29/6/2021
 */
class PackActivity : BaseMvvmActivity<ActivityPackBinding, PackViewModel>() {

    val url =
        "https://img1.baidu.com/it/u=61708474,3204071655&fm=26&fmt=auto&gp=0.jpg"
    val placeholder = R.drawable.vip_ic_pay_add

    override fun initViewModel(): PackViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(PackViewModel::class.java)
    }

    override fun bindViewModel(viewModel: PackViewModel) {
        viewModel.testJson()
        viewModel.testCache()
    }

    override val layoutId: Int
        get() = R.layout.activity_pack

    override fun initCustom() {
        super.initCustom()
        binding.activity = this
    }
}