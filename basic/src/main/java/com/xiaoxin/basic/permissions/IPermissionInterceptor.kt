package com.xiaoxin.basic.permissions

import android.app.Activity
import java.util.ArrayList

interface IPermissionInterceptor {
    /**
     * 权限申请拦截，可在此处先弹 Dialog 再申请权限
     */
    fun requestPermissions(activity: Activity?, callback: OnPermissionCallback?, allPermissions: List<String?>) {
        PermissionFragment.Companion.beginRequest(activity, ArrayList(allPermissions), this, callback)
    }

    /**
     * 权限授予回调拦截，参见 [OnPermissionCallback.onGranted]
     */
    fun grantedPermissions(
        activity: Activity?, allPermissions: List<String?>?,
        grantedPermissions: List<String?>?, all: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, all)
    }

    /**
     * 权限拒绝回调拦截，参见 [OnPermissionCallback.onDenied]
     */
    fun deniedPermissions(
        activity: Activity?, allPermissions: List<String?>?,
        deniedPermissions: List<String?>?, never: Boolean,
        callback: OnPermissionCallback?
    ) {
        if (callback == null) {
            return
        }
        callback.onDenied(deniedPermissions, never)
    }
}