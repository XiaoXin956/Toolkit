package com.xiaoxin.basic.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

internal object PermissionPageIntent {
    /**
     * 根据传入的权限自动选择最合适的权限设置页
     *
     * @param permissions                 请求失败的权限
     */
    fun getSmartPermissionIntent(context: Context, permissions: List<String?>?): Intent? {
        // 如果失败的权限里面不包含特殊权限
        if (((permissions == null) || permissions.isEmpty() ||
                    !PermissionApi.containsSpecialPermission(permissions))
        ) {
            return getApplicationDetailsIntent(context)
        }
        if ((AndroidVersion.isAndroid11 && (permissions.size == 3) &&
                    ((permissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) &&
                            permissions.contains(Permission.READ_EXTERNAL_STORAGE) &&
                            permissions.contains(Permission.WRITE_EXTERNAL_STORAGE))))
        ) {
            return getStoragePermissionIntent(context)
        }

        // 如果当前只有一个权限被拒绝了
        if (permissions.size == 1) {
            val permission: String? = permissions.get(0)
            if ((Permission.MANAGE_EXTERNAL_STORAGE == permission)) {
                return getStoragePermissionIntent(context)
            }
            if ((Permission.REQUEST_INSTALL_PACKAGES == permission)) {
                return getInstallPermissionIntent(context)
            }
            if ((Permission.SYSTEM_ALERT_WINDOW == permission)) {
                return getWindowPermissionIntent(context)
            }
            if ((Permission.WRITE_SETTINGS == permission)) {
                return getSettingPermissionIntent(context)
            }
            if ((Permission.NOTIFICATION_SERVICE == permission)) {
                return getNotifyPermissionIntent(context)
            }
            if ((Permission.PACKAGE_USAGE_STATS == permission)) {
                return getPackagePermissionIntent(context)
            }
            if ((Permission.BIND_NOTIFICATION_LISTENER_SERVICE == permission)) {
                return getNotificationListenerIntent(context)
            }
            if ((Permission.SCHEDULE_EXACT_ALARM == permission)) {
                return getAlarmPermissionIntent(context)
            }
            if ((Permission.ACCESS_NOTIFICATION_POLICY == permission)) {
                return getNotDisturbPermissionIntent(context)
            }
            if ((Permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS == permission)) {
                return getIgnoreBatteryPermissionIntent(context)
            }
        }
        return getApplicationDetailsIntent(context)
    }

    /**
     * 获取应用详情界面意图
     */
    fun getApplicationDetailsIntent(context: Context): Intent {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = getPackageNameUri(context)
        return intent
    }

    /**
     * 获取安装权限设置界面意图
     */
    fun getInstallPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid8) {
            intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = getPackageNameUri(context)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取悬浮窗权限设置界面意图
     */
    fun getWindowPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid6) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            // 在 Android 11 加包名跳转也是没有效果的，官方文档链接：
            // https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
            intent.setData(getPackageNameUri(context))
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取通知栏权限设置界面意图
     */
    fun getNotifyPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid8) {
            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
            //intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取通知监听设置界面意图
     */
    fun getNotificationListenerIntent(context: Context): Intent {
        var intent: Intent
        if (AndroidVersion.isAndroid5_1) {
            intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        }
        if (!PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取系统设置权限界面意图
     */
    fun getSettingPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid6) {
            intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = getPackageNameUri(context)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取存储权限设置界面意图
     */
    fun getStoragePermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid11) {
            intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.setData(getPackageNameUri(context))
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取使用统计权限设置界面意图
     */
    fun getPackagePermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid5) {
            intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            if (AndroidVersion.isAndroid10) {
                // 经过测试，只有在 Android 10 及以上加包名才有效果
                // 如果在 Android 10 以下加包名会导致无法跳转
                intent.data = getPackageNameUri(context)
            }
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取勿扰模式设置界面意图
     */
    fun getNotDisturbPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid6) {
            intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取电池优化选项设置界面意图
     */
    fun getIgnoreBatteryPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid6) {
            intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.setData(getPackageNameUri(context))
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取闹钟权限设置界面意图
     */
    fun getAlarmPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (AndroidVersion.isAndroid12) {
            intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.setData(getPackageNameUri(context))
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取包名 Uri 对象
     */
    private fun getPackageNameUri(context: Context): Uri {
        return Uri.parse("package:" + context.packageName)
    }
}