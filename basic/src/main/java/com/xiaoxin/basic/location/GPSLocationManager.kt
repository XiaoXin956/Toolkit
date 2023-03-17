package com.xiaoxin.basic.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.xiaoxin.basic.permissions.XXPermissions
import java.lang.ref.WeakReference

class GPSLocationManager(context: Activity) {

    companion object{

        var gpsLocationManager: GPSLocationManager? = null
    }

    val GPS_LOCATION_NAME: String = LocationManager.GPS_PROVIDER


    var objLock = Any()

    var mLocateType: String? = null

    var isGpsEnabled = false

    var mContext: WeakReference<Activity>? = null

    var locationManager: LocationManager? = null

    var gpsLocation: GPSLocation? = null

    var isOpenGps: Boolean = false

    var minTime: Long = 0L

    var minDistance: Float? = 0F

    init {
        initData(context)
    }

    fun getInstance(context: Activity): GPSLocationManager {
        if (gpsLocationManager == null) {
            synchronized(objLock) {
                if (gpsLocationManager == null) {
                    gpsLocationManager = GPSLocationManager(context)
                }
            }

        }
        return gpsLocationManager!!
    }

    fun initData(context: Activity){
        mContext = WeakReference(context)

        if (mContext?.get() != null) {
            locationManager = (mContext?.get()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        }
        mLocateType = LocationManager.NETWORK_PROVIDER
        isOpenGps = false
        minTime = 1000L
        minDistance = 0F

    }

    fun setScanSpan(minTime: Long) {
        this.minTime = minTime
    }

    fun setMinDistance(minDistance: Float) {
        this.minDistance = minDistance
    }

    fun start(gpsLocationListener: GPSLocationListener) {
        this.start(gpsLocationListener, isOpenGps)
    }

    fun start(gpsLocationListener: GPSLocationListener, isOpenGps: Boolean) {

        this.isOpenGps = isOpenGps

        if (mContext == null) {
            return
        }

        gpsLocation = GPSLocation(gpsLocationListener)
        isGpsEnabled = locationManager!!.isProviderEnabled(GPS_LOCATION_NAME)
        if (!isGpsEnabled && !isOpenGps) {
            openGps()
            return
        }

        // 申请权限
        val tempContext = mContext!!.get()!!
        if (ActivityCompat.checkSelfPermission(
                tempContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                tempContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // 判断是否打开了定位权限

        val lastKnownLocation = locationManager?.getLastKnownLocation(mLocateType!!)
        gpsLocation?.onLocationChanged(lastKnownLocation!!)
        locationManager?.requestLocationUpdates(mLocateType!!,minTime,minDistance!!,gpsLocation!!)
    }

    private fun openGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        mContext!!.get()?.startActivity(intent)
    }

    fun stop(){
        // 跳转到gps 打开页面
        if(mContext!!.get()!=null){
            locationManager?.removeUpdates(gpsLocation!!)
        }

    }


}