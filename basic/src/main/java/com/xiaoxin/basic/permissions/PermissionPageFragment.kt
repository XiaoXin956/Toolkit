package com.xiaoxin.basic.permissions

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import com.xiaoxin.basic.permissions.AndroidVersion
import com.xiaoxin.basic.permissions.OnPermissionCallback
import com.xiaoxin.basic.permissions.PermissionFragment
import com.xiaoxin.basic.permissions.PermissionApi
import com.xiaoxin.basic.permissions.PermissionUtils
import com.xiaoxin.basic.permissions.IPermissionInterceptor
import com.xiaoxin.basic.permissions.PermissionPageIntent
import com.xiaoxin.basic.permissions.OnPermissionPageCallback
import com.xiaoxin.basic.permissions.PermissionPageFragment
import com.xiaoxin.basic.permissions.XXPermissions
import java.util.ArrayList

class PermissionPageFragment constructor() : Fragment(), Runnable {
    /** 权限回调对象  */
    private var mCallBack: OnPermissionPageCallback? = null

    /** 权限申请标记  */
    private var mRequestFlag: Boolean = false

    /** 是否申请了权限  */
    private var mStartActivityFlag: Boolean = false

    /**
     * 绑定 Activity
     */
    fun attachActivity(activity: Activity) {
        activity.getFragmentManager().beginTransaction().add(this, this.toString()).commitAllowingStateLoss()
    }

    /**
     * 解绑 Activity
     */
    fun detachActivity(activity: Activity) {
        activity.getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss()
    }

    public override fun onResume() {
        super.onResume()

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            detachActivity(getActivity())
            return
        }
        if (mStartActivityFlag) {
            return
        }
        mStartActivityFlag = true
        val arguments: Bundle? = getArguments()
        val activity: Activity? = getActivity()
        if (arguments == null || activity == null) {
            return
        }
        val permissions: List<String?>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        startActivityForResult(PermissionPageIntent.getSmartPermissionIntent(getActivity(), permissions), XXPermissions.Companion.REQUEST_CODE)
    }

    /**
     * 设置权限监听回调监听
     */
    fun setCallBack(callback: OnPermissionPageCallback?) {
        mCallBack = callback
    }

    /**
     * 权限申请标记（防止系统杀死应用后重新触发请求的问题）
     */
    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode != XXPermissions.Companion.REQUEST_CODE) {
            return
        }
        val activity: Activity? = getActivity()
        val arguments: Bundle? = getArguments()
        if (activity == null || arguments == null) {
            return
        }
        val allPermissions: ArrayList<String?>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }
        PermissionUtils.postActivityResult(allPermissions, this)
    }

    public override fun run() {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        if (!isAdded()) {
            return
        }
        val activity: Activity? = getActivity()
        if (activity == null) {
            return
        }
        val callback: OnPermissionPageCallback? = mCallBack
        mCallBack = null
        if (callback == null) {
            detachActivity(getActivity())
            return
        }
        val arguments: Bundle = getArguments()
        val allPermissions: List<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        val grantedPermissions: List<String?>? = PermissionApi.getGrantedPermissions(activity, allPermissions)
        if (grantedPermissions!!.size == allPermissions!!.size) {
            callback.onGranted()
        } else {
            callback.onDenied()
        }
    }

    companion object {
        /** 请求的权限组  */
        private val REQUEST_PERMISSIONS: String = "request_permissions"

        /**
         * 开启权限申请
         */
        fun beginRequest(activity: Activity, permissions: ArrayList<String?>?, callback: OnPermissionPageCallback?) {
            val fragment: PermissionPageFragment = PermissionPageFragment()
            val bundle: Bundle = Bundle()
            bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions)
            fragment.setArguments(bundle)
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.setRetainInstance(true)
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            fragment.setCallBack(callback)
            // 绑定到 Activity 上面
            fragment.attachActivity(activity)
        }
    }
}