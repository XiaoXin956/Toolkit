package com.xiaoxin.basic.permissions

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.ArrayList

/**
 * Android 危险权限请求类
 */
class XXPermissions private constructor(context: Context) {
    /** Context 对象  */
    private val mContext: Context?

    /** 权限列表  */
    private var mPermissions: MutableList<String?>? = null

    /** 权限请求拦截器  */
    private var mInterceptor: IPermissionInterceptor? = null

    /** 设置不检查  */
    private var mCheckMode: Boolean? = null

    /**
     * 私有化构造函数
     */
    init {
        mContext = context
    }

    /**
     * 添加权限组
     */
    fun permission(vararg permissions: String): XXPermissions {
        return permission(PermissionUtils.asArrayList(*(permissions)))
    }

    fun permission(vararg permissions: Array<String?>): XXPermissions {
        return permission(PermissionUtils.asArrayLists(*permissions))
    }

    fun permission(permissions: List<String?>?): XXPermissions {
        if (permissions == null || permissions.isEmpty()) {
            return this
        }
        if (mPermissions == null) {
            mPermissions = ArrayList(permissions)
            return this
        }
        for (permission: String? in permissions) {
            if (mPermissions!!.contains(permission)) {
                continue
            }
            mPermissions!!.add(permission)
        }
        return this
    }

    /**
     * 设置权限请求拦截器
     */
    fun interceptor(interceptor: IPermissionInterceptor?): XXPermissions {
        mInterceptor = interceptor
        return this
    }

    /**
     * 设置不触发错误检测机制
     */
    fun unchecked(): XXPermissions {
        mCheckMode = false
        return this
    }

    /**
     * 请求权限
     */
    fun request(callback: OnPermissionCallback?) {
        if (mContext == null) {
            return
        }
        if (mInterceptor == null) {
            mInterceptor = interceptor
        }

        // 权限请求列表（为什么直接不用字段？因为框架要兼容新旧权限，在低版本下会自动添加旧权限申请）
        val permissions: MutableList<String?> = ArrayList(mPermissions)
        if (mCheckMode == null) {
            if (sCheckMode == null) {
                sCheckMode = PermissionUtils.isDebugMode(mContext)
            }
            mCheckMode = sCheckMode
        }

        // 检查当前 Activity 状态是否是正常的，如果不是则不请求权限
        val activity: Activity? = PermissionUtils.findActivity(mContext)
        if (!PermissionChecker.checkActivityStatus(activity, (mCheckMode)!!)) {
            return
        }

        // 必须要传入正常的权限或者权限组才能申请权限
        if (!PermissionChecker.checkPermissionArgument(permissions, (mCheckMode)!!)) {
            return
        }
        if ((mCheckMode)!!) {
            // 检查申请的存储权限是否符合规范
            PermissionChecker.checkStoragePermission(mContext, permissions)
            // 检查申请的定位权限是否符合规范
            PermissionChecker.checkLocationPermission(mContext, permissions)
            // 检查申请的权限和 targetSdk 版本是否能吻合
            PermissionChecker.checkTargetSdkVersion(mContext, permissions)
        }
        if ((mCheckMode)!!) {
            // 检测权限有没有在清单文件中注册
            PermissionChecker.checkManifestPermissions(mContext, permissions)
        }

        // 优化所申请的权限列表
        PermissionChecker.optimizeDeprecatedPermission(permissions)
        if (PermissionApi.isGrantedPermissions(mContext, permissions)) {
            // 证明这些权限已经全部授予过，直接回调成功
            if (callback != null) {
                mInterceptor!!.grantedPermissions(activity, permissions, permissions, true, callback)
            }
            return
        }

        // 申请没有授予过的权限
        mInterceptor!!.requestPermissions(activity, callback, permissions)
    }

    companion object {
        /** 权限设置页跳转请求码  */
        val REQUEST_CODE: Int = 1024 + 1

        /** 权限请求拦截器  */
        private var sInterceptor: IPermissionInterceptor? = null

        /** 当前是否为检查模式  */
        private var sCheckMode: Boolean? = null

        /**
         * 设置请求的对象
         *
         * @param context          当前 Activity，可以传入栈顶的 Activity
         */
        fun with(context: Context): XXPermissions {
            return XXPermissions(context)
        }

        fun with(fragment: Fragment): XXPermissions {
            return with(fragment.activity)
        }
        //    public static XXPermissions with(android.support.v4.app.Fragment fragment) {
        //        return with(fragment.getActivity());
        //    }
        /**
         * 是否为检查模式
         */
        fun setCheckMode(checkMode: Boolean) {
            sCheckMode = checkMode
        }
        /**
         * 获取全局权限请求拦截器
         */
        /**
         * 设置全局权限请求拦截器
         */
        var interceptor: IPermissionInterceptor?
            get() {
                if (sInterceptor == null) {
                    sInterceptor = object : IPermissionInterceptor {}
                }
                return sInterceptor
            }
            set(interceptor) {
                sInterceptor = interceptor
            }

        /**
         * 判断一个或多个权限是否全部授予了
         */
        fun isGranted(context: Context, vararg permissions: String?): Boolean {
            return isGranted(context, PermissionUtils.asArrayList(*(permissions)))
        }

        fun isGranted(context: Context, vararg permissions: Array<String?>): Boolean {
            return isGranted(context, PermissionUtils.asArrayLists(*permissions))
        }

        fun isGranted(context: Context, permissions: List<String?>?): Boolean {
            return PermissionApi.isGrantedPermissions(context, permissions)
        }

        /**
         * 获取没有授予的权限
         */
        fun getDenied(context: Context, vararg permissions: String?): List<String?>? {
            return getDenied(context, PermissionUtils.asArrayList(*(permissions)))
        }

        fun getDenied(context: Context, vararg permissions: Array<String?>): List<String?>? {
            return getDenied(context, PermissionUtils.asArrayLists(*permissions))
        }

        fun getDenied(context: Context, permissions: List<String?>?): List<String?>? {
            return PermissionApi.getDeniedPermissions(context, permissions)
        }

        /**
         * 判断某个权限是否为特殊权限
         */
        fun isSpecial(permission: String?): Boolean {
            return PermissionApi.isSpecialPermission(permission)
        }

        /**
         * 判断权限列表中是否包含特殊权限
         */
        fun containsSpecial(vararg permissions: String?): Boolean {
            return containsSpecial(PermissionUtils.asArrayList(*(permissions)))
        }

        fun containsSpecial(permissions: List<String?>?): Boolean {
            return PermissionApi.containsSpecialPermission(permissions)
        }

        /**
         * 判断一个或多个权限是否被永久拒绝了
         *
         * （注意不能在请求权限之前调用，应该在 [OnPermissionCallback.onDenied] 方法中调用）
         */
        fun isPermanentDenied(activity: Activity, vararg permissions: String?): Boolean {
            return isPermanentDenied(activity, PermissionUtils.asArrayList(*(permissions)))
        }

        fun isPermanentDenied(activity: Activity, vararg permissions: Array<String?>): Boolean {
            return isPermanentDenied(activity, PermissionUtils.asArrayLists(*permissions))
        }

        fun isPermanentDenied(activity: Activity, permissions: List<String?>?): Boolean {
            return PermissionApi.isPermissionPermanentDenied(activity, permissions)
        }

        fun startPermissionActivity(context: Context, vararg permissions: String?) {
            startPermissionActivity(context, PermissionUtils.asArrayList(*(permissions)))
        }

        fun startPermissionActivity(context: Context, vararg permissions: Array<String?>) {
            startPermissionActivity(context, PermissionUtils.asArrayLists(*permissions))
        }

        /**
         * 跳转到应用权限设置页
         *
         * @param permissions           没有授予或者被拒绝的权限组
         */
        /* android.content.Context */
        @JvmOverloads
        fun startPermissionActivity(context: Context, permissions: List<String?>? = null as List<String?>?) {
            val activity: Activity? = PermissionUtils.findActivity(context)
            if (activity != null) {
                startPermissionActivity(activity, permissions)
                return
            }
            val intent: Intent? = PermissionPageIntent.getSmartPermissionIntent(context, permissions)
            if (!(context is Activity)) {
                intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        fun startPermissionActivity(activity: Activity, vararg permissions: String?) {
            startPermissionActivity(activity, PermissionUtils.asArrayList(*(permissions)))
        }

        fun startPermissionActivity(activity: Activity, vararg permissions: Array<String?>) {
            startPermissionActivity(activity, PermissionUtils.asArrayLists(*permissions))
        }

        /* android.app.Activity */
        @JvmOverloads
        fun startPermissionActivity(activity: Activity, permissions: List<String?>? = null as List<String?>?, requestCode: Int = REQUEST_CODE) {
            activity.startActivityForResult(PermissionPageIntent.getSmartPermissionIntent(activity, permissions), requestCode)
        }

        fun startPermissionActivity(activity: Activity, permission: String?, callback: OnPermissionPageCallback?) {
            startPermissionActivity(activity, PermissionUtils.asArrayList(permission), callback)
        }

        fun startPermissionActivity(activity: Activity, permissions: Array<String>, callback: OnPermissionPageCallback?) {
            startPermissionActivity(activity, PermissionUtils.asArrayLists<String>(permissions), callback)
        }

        fun startPermissionActivity(activity: Activity, permissions: List<String?>?, callback: OnPermissionPageCallback?) {
            PermissionPageFragment.Companion.beginRequest(activity, permissions as ArrayList<String?>?, callback)
        }

        fun startPermissionActivity(fragment: Fragment, vararg permissions: String?) {
            startPermissionActivity(fragment, PermissionUtils.asArrayList(*(permissions)))
        }

        fun startPermissionActivity(fragment: Fragment, vararg permissions: Array<String?>) {
            startPermissionActivity(fragment, PermissionUtils.asArrayLists(*permissions))
        }

        /* android.app.Fragment */
        @JvmOverloads
        fun startPermissionActivity(fragment: Fragment, permissions: List<String?>? = null as List<String?>?, requestCode: Int = REQUEST_CODE) {
            val activity: Activity? = fragment.getActivity()
            if (activity == null) {
                return
            }
            fragment.startActivityForResult(PermissionPageIntent.getSmartPermissionIntent(activity, permissions), requestCode)
        }

        fun startPermissionActivity(fragment: Fragment, permission: String?, callback: OnPermissionPageCallback?) {
            startPermissionActivity(fragment, PermissionUtils.asArrayList(permission), callback)
        }

        fun startPermissionActivity(fragment: Fragment, permissions: Array<String>, callback: OnPermissionPageCallback?) {
            startPermissionActivity(fragment, PermissionUtils.asArrayLists<String>(permissions), callback)
        }

        fun startPermissionActivity(fragment: Fragment, permissions: List<String?>, callback: OnPermissionPageCallback?) {
            val activity: Activity? = fragment.getActivity()
            if (activity == null || activity.isFinishing()) {
                return
            }
            if (Build.VERSION.SDK_INT >= AndroidVersion.ANDROID_4_2 && activity.isDestroyed()) {
                return
            }
            PermissionPageFragment.Companion.beginRequest(activity, permissions as ArrayList<String?>?, callback)
        }
    }
}