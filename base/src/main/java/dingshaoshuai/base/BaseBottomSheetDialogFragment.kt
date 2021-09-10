package dingshaoshuai.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected var mActivity: BaseActivity? = null
    protected abstract val layoutId: Int
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 设置透明背景，以圆角生效
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogFragment_transparent_Theme)
        return super.onCreateDialog(savedInstanceState)
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