package dingshaoshuai.base.mvvm.page.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import dingshaoshuai.base.R
import dingshaoshuai.base.databinding.BaseListPageBinding
import dingshaoshuai.base.mvvm.page.BasePageMvvmActivity

abstract class BaseListPageMvvmActivity<
        VM : BaseListPageViewModel<*>,// 视图的 ViewModel
        E : RecyclerView.Adapter<*>// 本界面列表的 Adapter
        > : BasePageMvvmActivity<BaseListPageBinding, VM>() {
    override val contentLayoutId = R.layout.base_list_page
    protected lateinit var adapter: E

    protected abstract fun initAdapter(): E

    // 是否开启上拉加载更多操作
    protected open val enableRefreshLayoutLoadMore = false
    protected open fun createRefreshFooter() = ClassicsFooter(this)

    override fun initClickListener() {
        super.initClickListener()
        binding.refreshLayout.apply {
            setEnableLoadMore(enableRefreshLayoutLoadMore)
            if (enableRefreshLayoutLoadMore) {
                setRefreshFooter(createRefreshFooter())
                setOnLoadMoreListener {
                    viewModel.loadMoreData()
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.listPageChangeLiveData.loadMoreFinishLiveData.observe(this) {
            when (it) {
                BaseListPageViewModel.LoadMoreState.LoadMoreSuccess -> {
                    binding.refreshLayout.finishLoadMore(true)
                }
                BaseListPageViewModel.LoadMoreState.LoadMoreFailure -> {
                    binding.refreshLayout.finishLoadMore(false)
                }
                BaseListPageViewModel.LoadMoreState.LoadMoreNoMoreData -> {
                    binding.refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    protected open fun configRecyclerView(rvList: RecyclerView) {
        rvList.layoutManager = LinearLayoutManager(this)
        this.adapter = initAdapter()
        rvList.adapter = this.adapter
    }

    override fun initContentView() {
        super.initContentView()
        configRecyclerView(contentBinding.rvList)
    }
}