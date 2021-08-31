package dingshaoshuai.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import java.lang.reflect.Method

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutId: Int
    protected open val isScreenPortrait = true
    protected open val isFitsSystemWindows = true
    protected open val getStatusBarBgColor = android.R.color.transparent
    protected open val getNavigationBarColor = android.R.color.transparent
    protected open val isStatusBarDarkFont = true
    private var progressDialog: ProgressDialog? = null
    protected open val enableEditTextFocusChange = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScreenOrientation()
        initStatusBar()
        initContentView()
        initCustom()
        initClickListener()
        initData()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun initScreenOrientation() {
        if (!isScreenPortrait) return
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                // 处理 8.0以上系统 强制竖屏crash问题
                val method: Method =
                    Activity::class.java.getDeclaredMethod("convertFromTranslucent")
                method.isAccessible = true
                method.invoke(this)
            } catch (t: Throwable) {
                Log.e(localClassName, "initScreenOrientation: ${t.message}")
            }
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initStatusBar() {
        ImmersionBar.with(this).run {
            fitsSystemWindows(isFitsSystemWindows)
            statusBarColor(getStatusBarBgColor)
            statusBarDarkFont(isStatusBarDarkFont)
            navigationBarColor(getNavigationBarColor)
        }.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    protected abstract fun initContentView()
    protected open fun initCustom() {}
    protected open fun initClickListener() {}
    protected open fun initData() {}

    open fun showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.setCancelable(false)
        }
        progressDialog?.show()
    }

    open fun dismissLoadingDialog() {
        progressDialog?.dismiss()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!enableEditTextFocusChange) {
            return super.dispatchTouchEvent(ev)
        }
        if (ev?.action === MotionEvent.ACTION_UP) {
            val view = currentFocus
            if (isShouldHideInput(view, ev)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 点击其他区域，EditText 失去焦点并收起键盘
     */
    private fun isShouldHideInput(view: View?, ev: MotionEvent?): Boolean {
        view ?: return false
        ev ?: return false
        if (view !is EditText) return false
        val leftTop = intArrayOf(0, 0)
        // 获取输入框在当前屏幕上的位置
        view.getLocationOnScreen(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom: Int = top + view.getHeight()
        val right: Int = left + view.getWidth()
        // 获取光标(触摸点)在当前屏幕上的位置
        val locationX: Float = ev.rawX
        val locationY: Float = ev.rawY
        return if (locationX > left && locationX < right && locationY > top && locationY < bottom) {
            // 点击的是输入框区域，保留点击EditText事件
            false
        } else {
            // 失去焦点
            view.clearFocus()
            true
        }
    }
}