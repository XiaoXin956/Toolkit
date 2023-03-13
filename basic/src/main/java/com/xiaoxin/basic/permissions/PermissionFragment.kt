package com.xiaoxin.basic.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
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
import java.util.*

class PermissionFragment constructor() : Fragment(), Runnable {
    /** 是否申请了特殊权限  */
    private var mSpecialRequest: Boolean = false

    /** 是否申请了危险权限  */
    private var mDangerousRequest: Boolean = false

    /** 权限申请标记  */
    private var mRequestFlag: Boolean = false

    /** 权限回调对象  */
    private var mCallBack: OnPermissionCallback? = null

    /** 权限请求拦截器  */
    private var mInterceptor: IPermissionInterceptor? = null

    /** Activity 屏幕方向  */
    private var mScreenOrientation: Int = 0

    /**
     * 绑定 Activity
     */
    fun attachActivity(activity: Activity?) {
        activity!!.getFragmentManager().beginTransaction().add(this, this.toString()).commitAllowingStateLoss()
    }

    /**
     * 解绑 Activity
     */
    fun detachActivity(activity: Activity) {
        activity.getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss()
    }

    /**
     * 设置权限监听回调监听
     */
    fun setCallBack(callback: OnPermissionCallback?) {
        mCallBack = callback
    }

    /**
     * 权限申请标记（防止系统杀死应用后重新触发请求的问题）
     */
    fun setRequestFlag(flag: Boolean) {
        mRequestFlag = flag
    }

    /**
     * 设置权限请求拦截器
     */
    fun setInterceptor(interceptor: IPermissionInterceptor?) {
        mInterceptor = interceptor
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity? = getActivity()
        if (activity == null) {
            return
        }
        // 如果当前没有锁定屏幕方向就获取当前屏幕方向并进行锁定
        mScreenOrientation = activity.getRequestedOrientation()
        if (mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            return
        }

        // 锁定当前 Activity 方向
        PermissionUtils.lockActivityOrientation(activity)
    }

    public override fun onDetach() {
        super.onDetach()
        val activity: Activity? = getActivity()
        if ((activity == null) || (mScreenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) || (
                    activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        ) {
            return
        }
        // 为什么这里不用跟上面一样 try catch ？因为这里是把 Activity 方向取消固定，只有设置横屏或竖屏的时候才可能触发 crash
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }

    public override fun onDestroy() {
        super.onDestroy()
        // 取消引用监听器，避免内存泄漏
        mCallBack = null
    }

    public override fun onResume() {
        super.onResume()

        // 如果当前 Fragment 是通过系统重启应用触发的，则不进行权限申请
        if (!mRequestFlag) {
            detachActivity(getActivity())
            return
        }

        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mSpecialRequest) {
            return
        }
        mSpecialRequest = true
        requestSpecialPermission()
    }

    /**
     * 申请特殊权限
     */
    fun requestSpecialPermission() {
        val arguments: Bundle? = getArguments()
        val activity: Activity? = getActivity()
        if (arguments == null || activity == null) {
            return
        }
        val allPermissions: List<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)

        // 是否需要申请特殊权限
        var requestSpecialPermission: Boolean = false

        // 判断当前是否包含特殊权限
        for (permission: String in allPermissions!!) {
            if (!PermissionApi.isSpecialPermission(permission)) {
                continue
            }
            if (PermissionApi.isGrantedPermission(activity, permission)) {
                // 已经授予过了，可以跳过
                continue
            }
            if ((Permission.MANAGE_EXTERNAL_STORAGE == permission) && !AndroidVersion.isAndroid11) {
                // 当前必须是 Android 11 及以上版本，因为在旧版本上是拿旧权限做的判断
                continue
            }
            // 跳转到特殊权限授权页面
            startActivityForResult(
                PermissionPageIntent.getSmartPermissionIntent(
                    activity,
                    PermissionUtils.asArrayList(permission)
                ), getArguments().getInt(REQUEST_CODE)
            )
            requestSpecialPermission = true
        }
        if (requestSpecialPermission) {
            return
        }
        // 如果没有跳转到特殊权限授权页面，就直接申请危险权限
        requestDangerousPermission()
    }

    /**
     * 申请危险权限
     */
    fun requestDangerousPermission() {
        val activity: Activity? = getActivity()
        val arguments: Bundle? = getArguments()
        if (activity == null || arguments == null) {
            return
        }
        val requestCode: Int = arguments.getInt(REQUEST_CODE)
        val allPermissions: ArrayList<String>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }
        if (!AndroidVersion.isAndroid6) {
            // 如果是 Android 6.0 以下，没有危险权限的概念，则直接回调监听
            val grantResults: IntArray = IntArray(allPermissions.size)
            for (i in grantResults.indices) {
                grantResults[i] = if (PermissionApi.isGrantedPermission(activity, allPermissions.get(i))) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
            onRequestPermissionsResult(requestCode, allPermissions.toTypedArray(), grantResults)
            return
        }
        var locationPermission: ArrayList<String?>? = null
        // Android 10 定位策略发生改变，申请后台定位权限的前提是要有前台定位权限（授予了精确或者模糊任一权限）
        if (AndroidVersion.isAndroid10 && allPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            locationPermission = ArrayList()
            if (allPermissions.contains(Permission.ACCESS_COARSE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_COARSE_LOCATION)
            }
            if (allPermissions.contains(Permission.ACCESS_FINE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_FINE_LOCATION)
            }
        }
        if (!AndroidVersion.isAndroid10 || (locationPermission == null) || locationPermission.isEmpty()) {
            requestPermissions(allPermissions.toTypedArray(), getArguments().getInt(REQUEST_CODE))
            return
        }

        // 在 Android 10 的机型上，需要先申请前台定位权限，再申请后台定位权限
        beginRequest(activity, locationPermission,
            object : IPermissionInterceptor {}, object : OnPermissionCallback {
                public override fun onGranted(permissions: List<String?>?, all: Boolean) {
                    if (!all || !isAdded()) {
                        return
                    }

                    // 前台定位权限授予了，现在申请后台定位权限
                    beginRequest(activity,
                        PermissionUtils.asArrayList(Permission.ACCESS_BACKGROUND_LOCATION), object : IPermissionInterceptor {}, object : OnPermissionCallback {
                            public override fun onGranted(permissions: List<String?>?, all: Boolean) {
                                if (!all || !isAdded()) {
                                    return
                                }

                                // 前台定位权限和后台定位权限都授予了
                                val grantResults: IntArray = IntArray(allPermissions.size)
                                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
                                onRequestPermissionsResult(requestCode, allPermissions.toTypedArray<String>(), grantResults)
                            }

                            public override fun onDenied(permissions: List<String?>?, never: Boolean) {
                                if (!isAdded()) {
                                    return
                                }

                                // 后台定位授权失败，但是前台定位权限已经授予了
                                val grantResults: IntArray = IntArray(allPermissions.size)
                                for (i in allPermissions.indices) {
                                    grantResults[i] = if ((Permission.ACCESS_BACKGROUND_LOCATION == allPermissions.get(i))) PackageManager.PERMISSION_DENIED else PackageManager.PERMISSION_GRANTED
                                }
                                onRequestPermissionsResult(requestCode, allPermissions.toTypedArray<String>(), grantResults)
                            }
                        })
                }

                public override fun onDenied(permissions: List<String?>?, never: Boolean) {
                    if (!isAdded()) {
                        return
                    }

                    // 前台定位授权失败，并且无法申请后台定位权限
                    val grantResults: IntArray = IntArray(allPermissions.size)
                    Arrays.fill(grantResults, PackageManager.PERMISSION_DENIED)
                    onRequestPermissionsResult(requestCode, allPermissions.toTypedArray<String>(), grantResults)
                }
            })
    }

    public override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if ((permissions == null) || (permissions.size == 0) || (
                    grantResults == null) || (grantResults.size == 0)
        ) {
            return
        }
        val arguments: Bundle? = getArguments()
        val activity: Activity? = getActivity()
        if ((activity == null) || (arguments == null) || (mInterceptor == null) || (
                    requestCode != arguments.getInt(REQUEST_CODE))
        ) {
            return
        }
        val callback: OnPermissionCallback? = mCallBack
        mCallBack = null
        val interceptor: IPermissionInterceptor = mInterceptor!!
        mInterceptor = null

        // 优化权限回调结果
        PermissionUtils.optimizePermissionResults(activity, permissions, grantResults)

        // 将数组转换成 ArrayList
        val allPermissions: List<String?>? = PermissionUtils.asArrayList(*permissions)

        // 释放对这个请求码的占用
        REQUEST_CODE_ARRAY.remove(requestCode)
        // 将 Fragment 从 Activity 移除
        detachActivity(activity)

        // 获取已授予的权限
        val grantedPermissions: List<String?>? = PermissionApi.getGrantedPermissions(allPermissions, grantResults)

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermissions!!.size == allPermissions!!.size) {
            // 代表申请的所有的权限都授予了
            interceptor.grantedPermissions(activity, allPermissions, grantedPermissions, true, callback)
            return
        }

        // 获取被拒绝的权限
        val deniedPermissions: List<String?>? = PermissionApi.getDeniedPermissions(allPermissions, grantResults)

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        interceptor.deniedPermissions(
            activity, allPermissions, deniedPermissions,
            PermissionApi.isPermissionPermanentDenied(activity, deniedPermissions), callback
        )

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermissions.isEmpty()) {
            interceptor.grantedPermissions(activity, allPermissions, grantedPermissions, false, callback)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val activity: Activity? = getActivity()
        val arguments: Bundle? = getArguments()
        if ((activity == null) || (arguments == null) || mDangerousRequest || (
                    requestCode != arguments.getInt(REQUEST_CODE))
        ) {
            return
        }
        val allPermissions: ArrayList<String?>? = arguments.getStringArrayList(REQUEST_PERMISSIONS)
        if (allPermissions == null || allPermissions.isEmpty()) {
            return
        }
        mDangerousRequest = true
        PermissionUtils.postActivityResult(allPermissions, this)
    }

    public override fun run() {
        // 如果用户离开太久，会导致 Activity 被回收掉
        // 所以这里要判断当前 Fragment 是否有被添加到 Activity
        // 可在开发者模式中开启不保留活动来复现这个 Bug
        if (!isAdded()) {
            return
        }
        // 请求其他危险权限
        requestDangerousPermission()
    }

    companion object {
        /** 请求的权限组  */
        private val REQUEST_PERMISSIONS: String = "request_permissions"

        /** 请求码（自动生成） */
        private val REQUEST_CODE: String = "request_code"

        /** 权限请求码存放集合  */
        private val REQUEST_CODE_ARRAY: MutableList<Int> = ArrayList()

        /**
         * 开启权限申请
         */
        fun beginRequest(
            activity: Activity?, permissions: ArrayList<String?>?,
            interceptor: IPermissionInterceptor?, callback: OnPermissionCallback?
        ) {
            val fragment: PermissionFragment = PermissionFragment()
            val bundle: Bundle = Bundle()
            var requestCode: Int
            // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
            do {
                // 新版本的 Support 库限制请求码必须小于 65536
                // 旧版本的 Support 库限制请求码必须小于 256
                requestCode = Random().nextInt(Math.pow(2.0, 8.0).toInt())
            } while (REQUEST_CODE_ARRAY.contains(requestCode))
            // 标记这个请求码已经被占用
            REQUEST_CODE_ARRAY.add(requestCode)
            bundle.putInt(REQUEST_CODE, requestCode)
            bundle.putStringArrayList(REQUEST_PERMISSIONS, permissions)
            fragment.setArguments(bundle)
            // 设置保留实例，不会因为屏幕方向或配置变化而重新创建
            fragment.setRetainInstance(true)
            // 设置权限申请标记
            fragment.setRequestFlag(true)
            // 设置权限回调监听
            fragment.setCallBack(callback)
            // 设置权限请求拦截器
            fragment.setInterceptor(interceptor)
            // 绑定到 Activity 上面
            fragment.attachActivity(activity)
        }
    }
}