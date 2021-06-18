package dingshaoshuai.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScreenOrientation()
        initStatusBar()
        initContentView()
        initCustom()
        initClickListener()
        initData()
    }

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
}