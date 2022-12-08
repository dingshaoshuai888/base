package dingshaoshuai.base.mvvm.page.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dingshaoshuai.base.mvvm.page.BasePageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseListPageViewModel<T> : BasePageViewModel<List<T>?>() {
    val listPageChangeLiveData = PageChangeLiveData()

    private var currentPageIndex = 0
    protected val pageSize = 20

    // 加载更多数据回调的方法
    protected open suspend fun loadMoreData(page: Int, param: Any?): List<T>? = null
    override fun checkEmpty(data: List<T>?) = data.isNullOrEmpty()
    protected open fun checkNoMoreData(data: List<T>?) =
        data.isNullOrEmpty() || data.size >= pageSize

    inner class PageChangeLiveData {
        val loadMoreFinishLiveData by lazy { MutableLiveData<LoadMoreState>() }
    }

    override fun loadDataSuccess(data: List<T>?) {
        super.loadDataSuccess(data)
        currentPageIndex = 1
    }


    sealed class LoadMoreState {
        object LoadMoreSuccess : LoadMoreState()
        object LoadMoreFailure : LoadMoreState()
        object LoadMoreNoMoreData : LoadMoreState()
    }

    protected fun loadMoreDataSuccess(data: List<T>?) {
        val value = pageData.value ?: return
        if (data.isNullOrEmpty()) return
        currentPageIndex++
        if (value is MutableList) {
            value.addAll(data)
            _pageData.value = value
        }
    }

    fun loadMoreData() {
        viewModelScope.launch(Dispatchers.Main) {
            val value: List<T>?
            try {
                value = withContext(Dispatchers.IO) {
                    loadMoreData(currentPageIndex, initParam)
                }
                when {
                    checkNoMoreData(value) -> {
                        listPageChangeLiveData.loadMoreFinishLiveData.value =
                            LoadMoreState.LoadMoreNoMoreData
                    }
                    else -> {
                        loadMoreDataSuccess(value)
                        listPageChangeLiveData.loadMoreFinishLiveData.value = LoadMoreState.LoadMoreSuccess
                    }
                }
            } catch (e: Exception) {
                listPageChangeLiveData.loadMoreFinishLiveData.value = LoadMoreState.LoadMoreFailure
                Log.e("ViewModelKt", "refreshData: ${e.message}")
            }
        }
    }
}