package com.xiaoxin.basic.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.XmlResourceParser
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Surface
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.IllegalStateException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

internal object PermissionUtils {
    /** Handler 对象  */
    private val HANDLER: Handler = Handler(Looper.getMainLooper())

    /**
     * 延迟一段时间执行 OnActivityResult，避免有些机型明明授权了，但还是回调失败的问题
     */
    fun postActivityResult(permissions: List<String?>, runnable: Runnable?) {
        var delayMillis: Long
        if (AndroidVersion.isAndroid11) {
            delayMillis = 100
        } else {
            delayMillis = 200
        }
        val manufacturer: String = Build.MANUFACTURER.lowercase(Locale.getDefault())
        if (manufacturer.contains("huawei")) {
            // 需要加长时间等待，不然某些华为机型授权了但是获取不到权限
            if (AndroidVersion.isAndroid8) {
                delayMillis = 300
            } else {
                delayMillis = 500
            }
        } else if (manufacturer.contains("xiaomi")) {
            // 经过测试，发现小米 Android 11 及以上的版本，申请这个权限需要 1 秒钟才能判断到
            // 因为在 Android 10 的时候，这个特殊权限弹出的页面小米还是用谷歌原生的
            // 然而在 Android 11 之后的，这个权限页面被小米改成了自己定制化的页面
            // 测试了原生的模拟器和 vivo 云测并发现没有这个问题，所以断定这个 Bug 就是小米特有的
            if (AndroidVersion.isAndroid11 &&
                permissions.contains(Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            ) {
                delayMillis = 1000
            }
        }
        HANDLER.postDelayed((runnable)!!, delayMillis)
    }

    /**
     * 获取 Android 属性命名空间
     */
    val androidNamespace: String
        get() {
            return "http://schemas.android.com/apk/res/android"
        }

    /**
     * 当前是否处于 debug 模式
     */
    fun isDebugMode(context: Context): Boolean {
        return (context.getApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    /**
     * 返回应用程序在清单文件中注册的权限
     */
    fun getManifestPermissions(context: Context): HashMap<String, Int> {
        val manifestPermissions: HashMap<String, Int> = HashMap()
        val parser: XmlResourceParser? = parseAndroidManifest(context)
        if (parser != null) {
            try {
                do {
                    // 当前节点必须为标签头部
                    if (parser.getEventType() != XmlResourceParser.START_TAG) {
                        continue
                    }

                    // 当前标签必须为 uses-permission
                    if (!("uses-permission" == parser.getName())) {
                        continue
                    }
                    manifestPermissions.put(
                        parser.getAttributeValue(androidNamespace, "name"),
                        parser.getAttributeIntValue(androidNamespace, "maxSdkVersion", Int.MAX_VALUE)
                    )
                } while (parser.next() != XmlResourceParser.END_DOCUMENT)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } finally {
                parser.close()
            }
        }
        if (manifestPermissions.isEmpty()) {
            try {
                // 当清单文件没有注册任何权限的时候，那么这个数组对象就是空的
                // https://github.com/getActivity/XXPermissions/issues/35
                val requestedPermissions: Array<String>? = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_PERMISSIONS
                ).requestedPermissions
                if (requestedPermissions != null) {
                    for (permission: String in requestedPermissions) {
                        manifestPermissions.put(permission, Int.MAX_VALUE)
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
        return manifestPermissions
    }

    /**
     * 优化权限回调结果
     */
    fun optimizePermissionResults(activity: Activity, permissions: Array<String>, grantResults: IntArray) {
        for (i in permissions.indices) {
            var recheck: Boolean = false
            val permission: String = permissions.get(i)

            // 如果这个权限是特殊权限，那么就重新进行权限检测
            if (PermissionApi.isSpecialPermission(permission)) {
                recheck = true
            }

            // 重新检查 Android 12 的三个新权限
            if (!AndroidVersion.isAndroid12 &&
                (((Permission.BLUETOOTH_SCAN == permission) || (Permission.BLUETOOTH_CONNECT == permission) || (Permission.BLUETOOTH_ADVERTISE == permission)))
            ) {
                recheck = true
            }

            // 重新检查 Android 10.0 的三个新权限
            if (!AndroidVersion.isAndroid10 &&
                (((Permission.ACCESS_BACKGROUND_LOCATION == permission) || (Permission.ACTIVITY_RECOGNITION == permission) || (Permission.ACCESS_MEDIA_LOCATION == permission)))
            ) {
                recheck = true
            }

            // 重新检查 Android 9.0 的一个新权限
            if (!AndroidVersion.isAndroid9 && (Permission.ACCEPT_HANDOVER == permission)) {
                recheck = true
            }

            // 重新检查 Android 8.0 的两个新权限
            if (!AndroidVersion.isAndroid8 &&
                ((Permission.ANSWER_PHONE_CALLS == permission) || (Permission.READ_PHONE_NUMBERS == permission))
            ) {
                recheck = true
            }
            if (recheck) {
                grantResults[i] = if (PermissionApi.isGrantedPermission(activity, permission)) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
            }
        }
    }

    /**
     * 将数组转换成 ArrayList
     *
     * 这里解释一下为什么不用 Arrays.asList
     * 第一是返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList
     * 第二是返回的 ArrayList 对象是只读的，也就是不能添加任何元素，否则会抛异常
     */
    fun <T> asArrayList(vararg array: T): ArrayList<T> {
        val list: ArrayList<T> = ArrayList(array.size)
        if (array == null || array.size == 0) {
            return list
        }
        for (t: T in array) {
            list.add(t)
        }
        return list
    }

    @SafeVarargs
    fun <T> asArrayLists(vararg arrays: Array<T>): ArrayList<T> {
        val list: ArrayList<T> = ArrayList()
        if (arrays == null || arrays.size == 0) {
            return list
        }
        for (ts: Array<T> in arrays) {
            list.addAll(asArrayList(*ts))
        }
        return list
    }

    /**
     * 寻找上下文中的 Activity 对象
     */
    fun findActivity(context: Context?): Activity? {
        var context: Context? = context
        do {
            if (context is Activity) {
                return context
            } else if (context is ContextWrapper) {
                context = context.getBaseContext()
            } else {
                return null
            }
        } while (context != null)
        return null
    }

    /**
     * 获取当前应用 Apk 在 AssetManager 中的 Cookie，如果获取失败，则为 0
     */
    @SuppressLint("PrivateApi")
    fun findApkPathCookie(context: Context): Int {
        val assets: AssetManager = context.getAssets()
        val apkPath: String = context.getApplicationInfo().sourceDir
        try {
            // 为什么不直接通过反射 AssetManager.findCookieForPath 方法来判断？因为这个 API 属于反射黑名单，反射执行不了
            // 为什么不直接通过反射 AssetManager.addAssetPathInternal 这个非隐藏的方法来判断？因为这个也反射不了
            val method: Method = assets.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
            val cookie: Int = method.invoke(assets, apkPath) as Int
            if (cookie != null) {
                return cookie
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        // 获取失败
        return 0
    }

    /**
     * 解析清单文件
     */
    fun parseAndroidManifest(context: Context): XmlResourceParser? {
        val cookie: Int = findApkPathCookie(context)
        if (cookie == 0) {
            // 如果 cookie 为 0，证明获取失败，直接 return
            return null
        }
        try {
            val parser: XmlResourceParser = context.getAssets().openXmlResourceParser(cookie, "AndroidManifest.xml")
            do {
                // 当前节点必须为标签头部
                if (parser.getEventType() != XmlResourceParser.START_TAG) {
                    continue
                }
                if (("manifest" == parser.getName())) {
                    // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
                    // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
                    if (TextUtils.equals(
                            context.getPackageName(),
                            parser.getAttributeValue(null, "package")
                        )
                    ) {
                        return parser
                    }
                }
            } while (parser.next() != XmlResourceParser.END_DOCUMENT)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 判断是否适配了分区存储
     */
    fun isScopedStorage(context: Context): Boolean {
        try {
            val metaKey: String = "ScopedStorage"
            val metaData: Bundle? = context.getPackageManager().getApplicationInfo(
                context.getPackageName(), PackageManager.GET_META_DATA
            ).metaData
            if (metaData != null && metaData.containsKey(metaKey)) {
                return java.lang.Boolean.parseBoolean(metaData.get(metaKey).toString())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 锁定当前 Activity 的方向
     */
    @SuppressLint("SwitchIntDef")
    fun lockActivityOrientation(activity: Activity) {
        try {
            // 兼容问题：在 Android 8.0 的手机上可以固定 Activity 的方向，但是这个 Activity 不能是透明的，否则就会抛出异常
            // 复现场景：只需要给 Activity 主题设置 <item name="android:windowIsTranslucent">true</item> 属性即可
            when (activity.getResources().getConfiguration().orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> activity.setRequestedOrientation(if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                Configuration.ORIENTATION_PORTRAIT -> activity.setRequestedOrientation(if (isActivityReverse(activity)) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                else -> {}
            }
        } catch (e: IllegalStateException) {
            // java.lang.IllegalStateException: Only fullscreen activities can request orientation
            e.printStackTrace()
        }
    }

    /**
     * 判断 Activity 是否反方向旋转了
     */
    fun isActivityReverse(activity: Activity): Boolean {
        // 获取 Activity 旋转的角度
        val activityRotation: Int
        if (AndroidVersion.isAndroid11) {
            activityRotation = activity.display!!.rotation
        } else {
            activityRotation = activity.windowManager.defaultDisplay.rotation
        }
        when (activityRotation) {
            Surface.ROTATION_180, Surface.ROTATION_270 -> return true
            Surface.ROTATION_0, Surface.ROTATION_90 -> return false
            else -> return false
        }
    }

    /**
     * 判断这个意图的 Activity 是否存在
     */
    fun areActivityIntent(context: Context, intent: Intent?): Boolean {
        return !context.getPackageManager().queryIntentActivities((intent)!!, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()
    }
}