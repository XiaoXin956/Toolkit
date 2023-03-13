package com.xiaoxin.basic.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *
 * @案例
 * val appFrontBackHelper = AppFrontBackHelper()
 * appFrontBackHelper.register(this,
 * onFront = {
 *     ToastUtils.showShort("前台")
 * },
 * onBack = {
 *     ToastUtils.showShort("后台")
 * }
 * )
 *
 */
class AppFrontBackHelper {

    var onFront: (() -> Unit)? = null
    var onBack: (() -> Unit)? = null

    fun register(application: Application, onFront: (() -> Unit), onBack: (() -> Unit)) {
        this.onFront = onFront
        this.onBack = onBack
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    fun unRegister(application: Application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }


    private var activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

        var activityCount = 0

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            activityCount++
            if (activityCount == 1) {
                onFront?.invoke()
            }
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            activityCount--
            if (activityCount == 0) {
                onBack?.invoke()
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {

        }

    }

}