package dingshaoshuai.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.gyf.barlibrary.ImmersionBar
import dingshaoshuai.base.util.LogUtil
import java.lang.reflect.Method

/**
 * @author: Xiao Bo
 * @date: 10/6/2021
 */
abstract class BaseActivity : AppCompatActivity() {

    // 布局文件
    protected abstract val layoutId: Int

    // 是否强制竖屏
    protected open val isScreenPortrait = true

    // 是否填充顶部状态栏 - true（填充） false（沉浸式）
    protected open val isFitsSystemWindows = true

    // 状态栏背景色
    protected open val statusBarBgColor = android.R.color.transparent

    // 底部导航栏颜色
    protected open val navigationBarColor = android.R.color.white

    // 状态栏字体颜色
    protected open val isStatusBarDarkFont = true

    // 默认的加载对话框
    private var progressBar: ProgressBar? = null

    // 是否启动点击其他区域收起键盘
    protected open val enableEditTextFocusChange = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置一些需要监听声明周期的操作
        lifecycle.apply {
            // 添加日志打印，方便根据类名称快速定位当前界面
            addObserver(LogUtil)
            val application = application
            if (application is BaseApplication) {
                // 供 App 在 Activity 的生命周期里做特有的业务逻辑
                application.activityObserver?.let { addObserver(it) }
                // 如果不等于 null 说明应用在后台被 kill
                if (savedInstanceState != null) {
                    application.onKilledAfterResume(this@BaseActivity)
                    return
                }
            }
        }
        // 设置屏幕方向
        initScreenOrientation()
        // 设置状态栏
        initStatusBar()
        // 初始化布局
        initContentView()
        // 添加一些自定义的操作
        initCustom()
        // 添加监听事件
        initClickListener()
        // 初始化页面内的数据
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
            // 填充顶部状态栏区域
            fitsSystemWindows(isFitsSystemWindows)
            // 顶部状态栏颜色
            statusBarColor(statusBarBgColor)
            // 顶部状态栏字体颜色
            statusBarDarkFont(isStatusBarDarkFont)
            // 底部导航栏背景色
            navigationBarColor(navigationBarColor)
        }.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 页面销毁 ImmersionBar 也要销毁，防止内存泄漏
        ImmersionBar.with(this).destroy()
    }

    protected abstract fun initContentView()
    protected open fun initCustom() {}
    protected open fun initClickListener() {}
    protected open fun initData() {}

    open fun showLoadingDialog() {
        if (progressBar == null) {
            progressBar = ProgressBar(this)
            val decorView = window?.decorView
            if (decorView is FrameLayout) {
                progressBar?.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                ).apply { gravity = Gravity.CENTER }
                decorView.addView(progressBar)
            }
        }
        progressBar?.isVisible = true
    }

    open fun dismissLoadingDialog() {
        progressBar?.isVisible = false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 处理点击其他区域收起键盘操作
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

    // 点击其他区域，EditText 失去焦点并收起键盘
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