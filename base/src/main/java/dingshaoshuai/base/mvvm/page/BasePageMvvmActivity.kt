package dingshaoshuai.base.mvvm.page

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.ViewStubProxy
import com.scwang.smart.refresh.header.ClassicsHeader
import dingshaoshuai.base.R
import dingshaoshuai.base.databinding.BasePageBinding
import dingshaoshuai.base.mvvm.BaseMvvmActivity

/**
 * @author: Xiao Bo
 * @date: 2/9/2021
 */
abstract class BasePageMvvmActivity<
        CV : ViewDataBinding,// 内容视图的绑定类
        VM : BasePageViewModel<*>// 视图的 ViewModel
        > : BaseMvvmActivity<BasePageBinding, VM>() {

    override val layoutId = R.layout.base_page

    protected open fun initHeadView(): View? = null
    // 是否开启下拉刷新操作
    protected open val enableRefreshLayoutRefresh = true
    protected open fun createRefreshHeader() = ClassicsHeader(this)

    protected lateinit var contentBinding: CV

    // 内容区域的布局文件
    protected abstract val contentLayoutId: Int

    // 加载的 View
    protected open val loadingViewStub: ViewStubProxy?
        get() = binding.placeholderView.loadingViewStub

    protected open var loadingView: View? = null
        set(value) {
            field = value
            onLoadingViewInit(field)
        }

    protected open fun onLoadingViewInit(loadingView: View?) {}

    // 空的 View
    protected open val emptyViewStub: ViewStubProxy?
        get() = binding.placeholderView.emptyViewStub

    protected open var emptyView: View? = null
        set(value) {
            field = value
            onEmptyViewInit(field)
        }

    protected open fun onEmptyViewInit(emptyView: View?) {
        emptyView ?: return
        emptyView.findViewById<View>(R.id.btnRefresh).setOnClickListener {
            initData()
        }
    }

    // 错误的 View
    protected open val errorViewStub: ViewStubProxy?
        get() = binding.placeholderView.errorViewStub

    protected open var errorView: View? = null
        set(value) {
            field = value
            onErrorViewInit(field)
        }

    protected open fun onErrorViewInit(errorView: View?) {
        errorView ?: return
        errorView.findViewById<View>(R.id.btnRefresh).setOnClickListener {
            initData()
        }
    }

    // 成功的 View
    private val successView: View
        get() = binding.refreshLayout

    override fun initContentView() {
        super.initContentView()
        // 为布局添加头部 View
        binding.headView.addView(initHeadView())
        // 内容 View 的 Binding
        contentBinding = DataBindingUtil.inflate(layoutInflater, contentLayoutId, null, false)
        // 为布局添加内容 View
        binding.contentView.addView(contentBinding.root)
    }

    override fun initClickListener() {
        super.initClickListener()
        // 配置一些 refreshLayout 的操作
        binding.refreshLayout.apply {
            setEnableRefresh(enableRefreshLayoutRefresh)
            if (enableRefreshLayoutRefresh){
                setRefreshHeader(createRefreshHeader())
                setOnRefreshListener {
                    viewModel.refreshData()
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.pageChangeLiveData.let { pageChange ->
            if (loadingViewStub != null || loadingView != null) {
                pageChange.loadingPageLiveData.observe(this) {
                    showLoadingPage()
                }
            }
            if (emptyViewStub != null || emptyView != null) {
                pageChange.emptyPageLiveData.observe(this) {
                    showEmptyPage()
                }
            }
            if (errorViewStub != null || errorView != null) {
                pageChange.errorPageLiveData.observe(this) {
                    showErrorPage()
                }
            }
            pageChange.successPageLiveData.observe(this) {
                showSuccessPage()
            }
            pageChange.refreshFinishLiveData.observe(this) {
                binding.refreshLayout.finishRefresh(it)
            }
        }
    }

    // 初始化界面可能需要参数
    protected open fun getInitParam(): Any? = null

    override fun initData() {
        // 在执行 viewModel.initData 之前需要设置参数
        viewModel.setParam(getInitParam())
        super.initData()
    }

    // 显示加载界面
    protected open fun showLoadingPage() {
        loadingView = loadingView ?: if (loadingViewStub?.isInflated == false) {
            loadingViewStub?.viewStub?.inflate()
        } else {
            null
        }
        loadingView?.visibility = View.VISIBLE

        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        successView.visibility = View.GONE
    }

    // 显示空界面
    protected open fun showEmptyPage() {
        emptyView = emptyView ?: if (emptyViewStub?.isInflated == false) {
            emptyViewStub?.viewStub?.inflate()
        } else {
            null
        }
        emptyView?.visibility = View.VISIBLE

        loadingView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        successView.visibility = View.GONE
    }

    // 显示错误界面
    protected open fun showErrorPage() {
        errorView = errorView ?: if (errorViewStub?.isInflated == false) {
            errorViewStub?.viewStub?.inflate()
        } else {
            null
        }
        errorView?.visibility = View.VISIBLE

        loadingView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
        successView.visibility = View.GONE
    }

    // 显示成功界面
    protected open fun showSuccessPage() {
        loadingView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        successView.visibility = View.VISIBLE
    }
}