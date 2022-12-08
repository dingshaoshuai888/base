package dingshaoshuai.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import dingshaoshuai.base.util.LogUtil

abstract class BaseApplication : Application() {

    // 当 App 在后台被杀死，恢复进程的时候需要启动的 Activity 的 Class
    protected abstract val launcherActivityClazz: Class<*>
    protected abstract val isDebug: Boolean
    // 供使用者在 Activity、Fragment 生命周期里做 App 业务逻辑的特有操作
    open val activityObserver: LifecycleObserver? = null
    open val fragmentObserver: LifecycleObserver? = null

    override fun onCreate() {
        super.onCreate()
        context = this
        LogUtil.isDebug = isDebug
    }

    // 当 App 在后台被杀死，恢复进程的时候调用
    internal fun onKilledAfterResume(activity: Activity) {
        val intent = Intent().apply {
            setClass(activity, launcherActivityClazz)
            // 携带参数
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        activity.startActivity(intent)
        activity.finish()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}