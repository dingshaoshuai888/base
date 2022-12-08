package dingshaoshuai.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dingshaoshuai.base.util.LogUtil

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseFragment : Fragment() {

    // 承载此 Fragment 的 Activity
    protected var mActivity: BaseActivity? = null
    // 布局文件
    protected abstract val layoutId: Int
    // 设置布局
    protected abstract fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    protected open fun initView(view: View) {}
    protected open fun initCustom(view: View) {}
    protected open fun initClickListener(view: View) {}
    protected open fun initData() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = activity
        if (activity is BaseActivity){
            mActivity = activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置一些需要监听声明周期的操作
        lifecycle.apply {
            // 添加日志打印，方便根据类名称快速定位当前界面
            addObserver(LogUtil)
            val application = mActivity?.application
            if (application is BaseApplication) {
                // 供 App 在 Fragment 的生命周期里做特有的业务逻辑
                application.fragmentObserver?.let { addObserver(it) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initContentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initCustom(view)
        initClickListener(view)
        initData()
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }
}