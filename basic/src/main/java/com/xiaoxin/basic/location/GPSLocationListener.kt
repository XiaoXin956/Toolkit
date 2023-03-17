package com.xiaoxin.basic.location

import android.location.GnssStatus
import android.location.Location
import android.os.Bundle

interface GPSLocationListener {

    // 位置信息发生改变
    fun updateLocation(location: Location)

    // provider 定位源类型变化时被调用
    fun updateStatus(provider: String, status: Int, extras: Bundle)

    // GPS状态发生改变时被调用（GPS手动启动、手动关闭、GPS不在服务区、GPS占时不可用、GPS可用)
    fun updateGPSProviderStatus(gpsStatus: Int)

}