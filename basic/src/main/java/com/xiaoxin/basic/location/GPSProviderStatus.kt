package com.xiaoxin.basic.location

object GPSProviderStatus {

    //用户手动开启GPS
    val GPS_ENABLED = 0

    //用户手动关闭GPS
    val GPS_DISABLED = 1

    //服务已停止，并且在短时间内不会改变
    val GPS_OUT_OF_SERVICE = 2

    //服务暂时停止，并且在短时间内会恢复
    val GPS_TEMPORARILY_UNAVAILABLE = 3

    //服务正常有效
    val GPS_AVAILABLE = 4

}