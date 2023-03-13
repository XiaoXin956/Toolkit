package com.xiaoxin.basic.permissions

import android.os.Build

internal object AndroidVersion {
    const val ANDROID_12_L = Build.VERSION_CODES.S_V2
    const val ANDROID_12 = Build.VERSION_CODES.S
    const val ANDROID_11 = Build.VERSION_CODES.R
    const val ANDROID_10 = Build.VERSION_CODES.Q
    const val ANDROID_9 = Build.VERSION_CODES.P
    const val ANDROID_8_1 = Build.VERSION_CODES.O_MR1
    const val ANDROID_8 = Build.VERSION_CODES.O
    const val ANDROID_7_1 = Build.VERSION_CODES.N_MR1
    const val ANDROID_7 = Build.VERSION_CODES.N
    const val ANDROID_6 = Build.VERSION_CODES.M
    const val ANDROID_5_1 = Build.VERSION_CODES.LOLLIPOP_MR1
    const val ANDROID_5 = Build.VERSION_CODES.LOLLIPOP
    const val ANDROID_4_4 = Build.VERSION_CODES.KITKAT
    const val ANDROID_4_3 = Build.VERSION_CODES.JELLY_BEAN_MR2
    const val ANDROID_4_2 = Build.VERSION_CODES.JELLY_BEAN_MR1
    const val ANDROID_4_1 = Build.VERSION_CODES.JELLY_BEAN

    /**
     * 是否是 Android 12 及以上版本
     */
    val isAndroid12: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_12

    /**
     * 是否是 Android 11 及以上版本
     */
    val isAndroid11: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_11

    /**
     * 是否是 Android 10 及以上版本
     */
    val isAndroid10: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_10

    /**
     * 是否是 Android 9.0 及以上版本
     */
    val isAndroid9: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_9

    /**
     * 是否是 Android 8.0 及以上版本
     */
    val isAndroid8: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_8

    /**
     * 是否是 Android 6.0 及以上版本
     */
    val isAndroid6: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_6

    /**
     * 是否是 Android 5.0 及以上版本
     */
    val isAndroid5_1: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_5_1

    /**
     * 是否是 Android 5.0 及以上版本
     */
    val isAndroid5: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_5

    /**
     * 是否是 Android 4.3 及以上版本
     */
    val isAndroid4_3: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_4_3

    /**
     * 是否是 Android 4.2 及以上版本
     */
    val isAndroid4_2: Boolean
        get() = Build.VERSION.SDK_INT >= ANDROID_4_2
}