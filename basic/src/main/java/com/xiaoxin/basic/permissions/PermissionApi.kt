package com.xiaoxin.basic.permissions

import android.app.Activity
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import java.util.ArrayList

internal object PermissionApi {
    /**
     * 是否有存储权限
     */
    fun isGrantedStoragePermission(context: Context): Boolean {
        return if (AndroidVersion.isAndroid11) {
            Environment.isExternalStorageManager()
        } else isGrantedPermissions(context, PermissionUtils.asArrayList(*Permission.Group.STORAGE))
    }

    /**
     * 是否有安装权限
     */
    fun isGrantedInstallPermission(context: Context): Boolean {
        return if (AndroidVersion.isAndroid8) {
            context.packageManager.canRequestPackageInstalls()
        } else true
    }

    /**
     * 是否有悬浮窗权限
     */
    fun isGrantedWindowPermission(context: Context?): Boolean {
        return if (AndroidVersion.isAndroid6) {
            Settings.canDrawOverlays(context)
        } else true
    }

    /**
     * 是否有系统设置权限
     */
    fun isGrantedSettingPermission(context: Context?): Boolean {
        return if (AndroidVersion.isAndroid6) {
            Settings.System.canWrite(context)
        } else true
    }

    /**
     * 是否有通知栏权限
     */
    fun isGrantedNotifyPermission(context: Context?): Boolean {
        return NotificationManagerCompat.from(context!!).areNotificationsEnabled()
    }

    /**
     * 是否通知栏监听的权限
     */
    fun isGrantedNotificationListenerPermission(context: Context): Boolean {
        if (AndroidVersion.isAndroid4_3) {
            val packageNames = NotificationManagerCompat.getEnabledListenerPackages(context)
            return packageNames.contains(context.packageName)
        }
        return true
    }

    /**
     * 是否有使用统计权限
     */
    fun isGrantedPackagePermission(context: Context): Boolean {
        if (AndroidVersion.isAndroid5) {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode: Int
            mode = if (AndroidVersion.isAndroid10) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    context.applicationInfo.uid, context.packageName
                )
            } else {
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    context.applicationInfo.uid, context.packageName
                )
            }
            return mode == AppOpsManager.MODE_ALLOWED
        }
        return true
    }

    /**
     * 是否有闹钟权限
     */
    fun isGrantedAlarmPermission(context: Context): Boolean {
        return if (AndroidVersion.isAndroid12) {
            context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
        } else true
    }

    /**
     * 是否有勿扰模式权限
     */
    fun isGrantedNotDisturbPermission(context: Context): Boolean {
        return if (AndroidVersion.isAndroid6) {
            context.getSystemService(NotificationManager::class.java).isNotificationPolicyAccessGranted
        } else true
    }

    /**
     * 是否忽略电池优化选项
     */
    fun isGrantedIgnoreBatteryPermission(context: Context): Boolean {
        return if (AndroidVersion.isAndroid6) {
            context.getSystemService(PowerManager::class.java).isIgnoringBatteryOptimizations(context.packageName)
        } else true
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    fun containsSpecialPermission(permissions: List<String?>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }
        for (permission in permissions) {
            if (isSpecialPermission(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    fun isSpecialPermission(permission: String?): Boolean {
        return Permission.MANAGE_EXTERNAL_STORAGE == permission || Permission.REQUEST_INSTALL_PACKAGES == permission || Permission.SYSTEM_ALERT_WINDOW == permission || Permission.WRITE_SETTINGS == permission || Permission.NOTIFICATION_SERVICE == permission || Permission.PACKAGE_USAGE_STATS == permission || Permission.SCHEDULE_EXACT_ALARM == permission || Permission.BIND_NOTIFICATION_LISTENER_SERVICE == permission || Permission.ACCESS_NOTIFICATION_POLICY == permission || Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS == permission
    }

    /**
     * 判断某些权限是否全部被授予
     */
    fun isGrantedPermissions(context: Context, permissions: List<String?>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }
        for (permission in permissions) {
            if (!isGrantedPermission(context, permission)) {
                return false
            }
        }
        return true
    }

    /**
     * 获取已经授予的权限
     */
    fun getGrantedPermissions(context: Context, permissions: List<String>?): List<String> {
        val grantedPermission: MutableList<String> = ArrayList(permissions!!.size)
        for (permission in permissions) {
            if (isGrantedPermission(context, permission)) {
                grantedPermission.add(permission)
            }
        }
        return grantedPermission
    }

    /**
     * 获取已经拒绝的权限
     */
    fun getDeniedPermissions(context: Context, permissions: List<String?>?): List<String?> {
        val deniedPermission: MutableList<String?> = ArrayList(permissions!!.size)
        for (permission in permissions) {
            if (!isGrantedPermission(context, permission)) {
                deniedPermission.add(permission)
            }
        }
        return deniedPermission
    }

    /**
     * 判断某个权限是否授予
     */
    fun isGrantedPermission(context: Context, permission: String?): Boolean {
        // 检测通知栏权限
        if (Permission.NOTIFICATION_SERVICE == permission) {
            return isGrantedNotifyPermission(context)
        }

        // 检测获取使用统计权限
        if (Permission.PACKAGE_USAGE_STATS == permission) {
            return isGrantedPackagePermission(context)
        }

        // 检测通知栏监听权限
        if (Permission.BIND_NOTIFICATION_LISTENER_SERVICE == permission) {
            return isGrantedNotificationListenerPermission(context)
        }

        // 其他权限在 Android 6.0 以下版本就默认授予
        if (!AndroidVersion.isAndroid6) {
            return true
        }

        // 检测存储权限
        if (Permission.MANAGE_EXTERNAL_STORAGE == permission) {
            return isGrantedStoragePermission(context)
        }

        // 检测安装权限
        if (Permission.REQUEST_INSTALL_PACKAGES == permission) {
            return isGrantedInstallPermission(context)
        }

        // 检测悬浮窗权限
        if (Permission.SYSTEM_ALERT_WINDOW == permission) {
            return isGrantedWindowPermission(context)
        }

        // 检测系统权限
        if (Permission.WRITE_SETTINGS == permission) {
            return isGrantedSettingPermission(context)
        }

        // 检测闹钟权限
        if (Permission.SCHEDULE_EXACT_ALARM == permission) {
            return isGrantedAlarmPermission(context)
        }

        // 检测勿扰权限
        if (Permission.ACCESS_NOTIFICATION_POLICY == permission) {
            return isGrantedNotDisturbPermission(context)
        }

        // 检测电池优化选项权限
        if (Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS == permission) {
            return isGrantedIgnoreBatteryPermission(context)
        }

        // 检测 Android 12 的三个新权限
        if (!AndroidVersion.isAndroid12) {
            if (Permission.BLUETOOTH_SCAN == permission) {
                return context.checkSelfPermission(Permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.BLUETOOTH_CONNECT == permission || Permission.BLUETOOTH_ADVERTISE == permission) {
                return true
            }
        }

        // 检测 Android 10 的三个新权限
        if (!AndroidVersion.isAndroid10) {
            if (Permission.ACCESS_BACKGROUND_LOCATION == permission) {
                return context.checkSelfPermission(Permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.ACTIVITY_RECOGNITION == permission) {
                return context.checkSelfPermission(Permission.BODY_SENSORS) ==
                        PackageManager.PERMISSION_GRANTED
            }
            if (Permission.ACCESS_MEDIA_LOCATION == permission) {
                return true
            }
        }

        // 检测 Android 9.0 的一个新权限
        if (!AndroidVersion.isAndroid9) {
            if (Permission.ACCEPT_HANDOVER == permission) {
                return true
            }
        }

        // 检测 Android 8.0 的两个新权限
        if (!AndroidVersion.isAndroid8) {
            if (Permission.ANSWER_PHONE_CALLS == permission) {
                return true
            }
            if (Permission.READ_PHONE_NUMBERS == permission) {
                return context.checkSelfPermission(Permission.READ_PHONE_STATE) ==
                        PackageManager.PERMISSION_GRANTED
            }
        }
        return context.checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    fun isPermissionPermanentDenied(activity: Activity, permissions: List<String?>?): Boolean {
        for (permission in permissions!!) {
            if (isPermissionPermanentDenied(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permission            请求的权限
     */
    fun isPermissionPermanentDenied(activity: Activity, permission: String?): Boolean {
        // 特殊权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回 false
        if (isSpecialPermission(permission)) {
            return false
        }
        if (!AndroidVersion.isAndroid6) {
            return false
        }

        // 检测 Android 12 的三个新权限
        if (!AndroidVersion.isAndroid12) {
            if (Permission.BLUETOOTH_SCAN == permission) {
                return !isGrantedPermission(activity, Permission.ACCESS_COARSE_LOCATION) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_COARSE_LOCATION)
            }
            if (Permission.BLUETOOTH_CONNECT == permission || Permission.BLUETOOTH_ADVERTISE == permission) {
                return false
            }
        }
        if (AndroidVersion.isAndroid10) {

            // 重新检测后台定位权限是否永久拒绝
            if (Permission.ACCESS_BACKGROUND_LOCATION == permission &&
                !isGrantedPermission(activity, Permission.ACCESS_BACKGROUND_LOCATION) &&
                !isGrantedPermission(activity, Permission.ACCESS_FINE_LOCATION)
            ) {
                return !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION)
            }
        }

        // 检测 Android 10 的三个新权限
        if (!AndroidVersion.isAndroid10) {
            if (Permission.ACCESS_BACKGROUND_LOCATION == permission) {
                return !isGrantedPermission(activity, Permission.ACCESS_FINE_LOCATION) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.ACCESS_FINE_LOCATION)
            }
            if (Permission.ACTIVITY_RECOGNITION == permission) {
                return !isGrantedPermission(activity, Permission.BODY_SENSORS) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.BODY_SENSORS)
            }
            if (Permission.ACCESS_MEDIA_LOCATION == permission) {
                return false
            }
        }

        // 检测 Android 9.0 的一个新权限
        if (!AndroidVersion.isAndroid9) {
            if (Permission.ACCEPT_HANDOVER == permission) {
                return false
            }
        }

        // 检测 Android 8.0 的两个新权限
        if (!AndroidVersion.isAndroid8) {
            if (Permission.ANSWER_PHONE_CALLS == permission) {
                return false
            }
            if (Permission.READ_PHONE_NUMBERS == permission) {
                return !isGrantedPermission(activity, Permission.READ_PHONE_STATE) &&
                        !activity.shouldShowRequestPermissionRationale(Permission.READ_PHONE_STATE)
            }
        }
        return !isGrantedPermission(activity, permission) &&
                !activity.shouldShowRequestPermissionRationale(permission!!)
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions           需要请求的权限组
     * @param grantResults          允许结果组
     */
    fun getDeniedPermissions(permissions: List<String?>?, grantResults: IntArray): List<String?> {
        val deniedPermissions: MutableList<String?> = ArrayList()
        for (i in grantResults.indices) {
            // 把没有授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions!![i])
            }
        }
        return deniedPermissions
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions       需要请求的权限组
     * @param grantResults      允许结果组
     */
    fun getGrantedPermissions(permissions: List<String?>?, grantResults: IntArray): List<String?> {
        val grantedPermissions: MutableList<String?> = ArrayList()
        for (i in grantResults.indices) {
            // 把授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions!![i])
            }
        }
        return grantedPermissions
    }
}