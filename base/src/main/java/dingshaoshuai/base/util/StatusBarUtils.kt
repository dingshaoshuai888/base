package dingshaoshuai.base.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.IntDef

@SuppressLint("ObsoleteSdkInt")
object StatusBarUtils {

    const val OTHER = -1
    const val MIUI = 1
    const val FLYME = 2
    const val ANDROID_M = 3

    @IntDef(OTHER, MIUI, FLYME, ANDROID_M)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class SystemType

    // 修改底部导航栏颜色
    fun setNavColor(activity: Activity, @ColorInt color: Int) {
        activity.window.navigationBarColor = color
    }

    // 修改状态栏为全透明（沉浸）
    fun setStatusBarTranslucent(activity: Activity) {
        // 先清除掉全屏模式
        clearFullScreen(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    // 只设置沉浸
    fun setStatusBarByFlags(activity: Activity) {
        // 先清除掉全屏模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    // 设置透明
    fun setTransparentForWindow(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window
                .decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
        }
    }

    // 设置状态栏字体颜色
    fun setStatusBarForegroundColor(activity: Activity?, isBlack: Boolean) {
        activity ?: return
        // 优先处理6.0以上的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarLightMode(activity, ANDROID_M, isBlack)
            return
        }
        // 小米系统
        if (setMIUIStatusBarLightMode(activity, isBlack)) return
        // 魅族系统
        if (setFlymeStatusBarLightMode(activity.window, isBlack)) return
    }

    // 设置状态栏颜色
    fun setStatusBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = color
        }
    }

    // 设置全屏模式
    fun setFullScreen(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    // 清除全屏模式
    fun clearFullScreen(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    /**
     * 获取系统状态栏的高度
     * 获取status_bar_height资源的ID
     * 根据资源ID获取响应的尺寸值
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        // 如果版本小于21就直接返回0，不需要设置状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            statusBarHeight = 0
        }
        return statusBarHeight
    }

    // 将 view 的高度设置成与状态栏一样高
    fun setStatusBarHeight(viewStateBar: View?) {
        viewStateBar ?: return
        val layoutParams = viewStateBar.layoutParams
        val height = getStatusBarHeight(viewStateBar.context)
        if (height > 0) {
            layoutParams.height = height
        }

        viewStateBar.layoutParams = layoutParams
    }

    /**
     * Android 6.0以上修改状态栏字体颜色
     */
    private fun setAndroidMStatusBarLightMode(window: Window, isBlack: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (isBlack) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = vis
        }
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    @SuppressLint("SwitchIntDef")
    private fun setStatusBarLightMode(activity: Activity, @SystemType type: Int, isBlack: Boolean) {
        when (type) {
            MIUI -> setMIUIStatusBarLightMode(activity, isBlack)
            FLYME -> setFlymeStatusBarLightMode(activity.window, isBlack)
            ANDROID_M -> setAndroidMStatusBarLightMode(activity.window, isBlack)
        }
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun setFlymeStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param activity 用于获取window和decorView
     * @param dark     是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @SuppressLint("PrivateApi")
    private fun setMIUIStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField =
                    clazz.getMethod(
                        "setExtraFlags",
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setAndroidMStatusBarLightMode(activity.window, dark)
                }
            } catch (e: Exception) {

            }

        }
        return result
    }
}