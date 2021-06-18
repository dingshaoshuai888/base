package dingshaoshuai.base

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dingshaoshuai.base.databinding.ActivityMainBinding
import dingshaoshuai.base.mvvm.BaseMvvmActivity

class MainActivity : BaseMvvmActivity<ActivityMainBinding, MainViewModel>() {
    override fun initViewModel(): MainViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)
    }

    override fun bindViewModel(viewModel: MainViewModel) {
        binding.viewModel = viewModel
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initObserver() {
        super.initObserver()
        viewModel.liveData.observe(this, Observer {
            Log.i(TAG, "initObserver: 发生了变化")
        })
        viewModel.callLiveData.observe(this, Observer {
            Log.i(TAG, "initObserver: call")
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}