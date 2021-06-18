package dingshaoshuai.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseFragment : Fragment() {

    protected var mActivity: BaseActivity? = null
    protected abstract val layoutId: Int
    protected abstract fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    protected open fun initView() {}
    protected open fun initCustom() {}
    protected open fun initClickListener() {}
    protected open fun initData() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = activity
        if (activity is BaseActivity){
            mActivity = activity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initContentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCustom()
        initClickListener()
        initData()
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }
}