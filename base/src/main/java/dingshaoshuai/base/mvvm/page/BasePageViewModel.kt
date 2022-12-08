package dingshaoshuai.base.mvvm.page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dingshaoshuai.base.mvvm.BaseViewModel
import dingshaoshuai.base.mvvm.CallLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: Xiao Bo
 * @date: 18/6/2021
 */
abstract class BasePageViewModel<T> : BaseViewModel() {
    val pageChangeLiveData = PageChangeLiveData()
    protected val _pageData = MutableLiveData<T>()
    val pageData: LiveData<T> = _pageData

    // 初始化数据需要的参数，不需要参数可以为 null
    protected var initParam: Any? = null

    // 设置为 open 可重写是因为有可能参数不需要 View 层传入，可以直接重写定义
    open fun setParam(param: Any?) {
        this.initParam = param
    }

    // 初始化数据回调的方法
    protected abstract suspend fun loadData(param: Any?): T
    // 检查数据是不是为空
    protected abstract fun checkEmpty(data: T?): Boolean
    // 初始化数据或者下拉刷新成功回调的方法
    protected open fun loadDataSuccess(data: T?){
        _pageData.value = data
    }

    inner class PageChangeLiveData {
        val loadingPageLiveData by lazy { CallLiveData() }

        val emptyPageLiveData by lazy { CallLiveData() }

        val errorPageLiveData by lazy { CallLiveData() }

        val successPageLiveData by lazy { CallLiveData() }

        val refreshFinishLiveData by lazy { MutableLiveData<Boolean>() }
    }

    private fun showLoadingPage() {
        pageChangeLiveData.loadingPageLiveData.call()
    }

    private fun showEmptyPage() {
        pageChangeLiveData.emptyPageLiveData.call()
    }

    private fun showErrorPage() {
        pageChangeLiveData.errorPageLiveData.call()
    }

    private fun showSuccessPage() {
        pageChangeLiveData.successPageLiveData.call()
    }

    override fun initData() {
        viewModelScope.launch(Dispatchers.Main) {
            showLoadingPage()
            val value: T?
            try {
                value = withContext(Dispatchers.IO) {
                    loadData(initParam)
                }
                when {
                    checkEmpty(value) -> {
                        showEmptyPage()
                    }
                    else -> {
                        showSuccessPage()
                        loadDataSuccess(value)
                    }
                }
            } catch (e: Exception) {
                showErrorPage()
                Log.e("ViewModelKt", "initData: ${e.message}")
            }
        }
    }

    fun refreshData(){
        viewModelScope.launch(Dispatchers.Main) {
            val value: T?
            try {
                value = withContext(Dispatchers.IO) {
                    loadData(initParam)
                }
                when {
                    checkEmpty(value) -> {
                        showEmptyPage()
                    }
                    else -> {
                        loadDataSuccess(value)
                    }
                }
                pageChangeLiveData.refreshFinishLiveData.value = true
            } catch (e: Exception) {
                pageChangeLiveData.refreshFinishLiveData.value = false
                Log.e("ViewModelKt", "refreshData: ${e.message}")
            }
        }
    }
}