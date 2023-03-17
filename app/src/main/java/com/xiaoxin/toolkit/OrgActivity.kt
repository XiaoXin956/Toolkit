package com.xiaoxin.toolkit

import android.app.Activity
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.xiaoxin.basic.location.GPSLocationListener
import com.xiaoxin.basic.location.GPSLocationManager
import com.xiaoxin.basic.location.GPSUtil
import com.xiaoxin.basic.permissions.OnPermissionCallback
import com.xiaoxin.basic.permissions.Permission
import com.xiaoxin.basic.permissions.XXPermissions

class OrgActivity : Activity() ,GPSLocationListener {


    var btn_location: Button?=null
    var activity:Activity?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org)

        activity = this

        btn_location = findViewById(R.id.btn_location)
        btn_location?.setOnClickListener {


            XXPermissions.with(this)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(object :OnPermissionCallback{
                    override fun onGranted(permissions: List<String?>?, all: Boolean) {
                        val gpsLocationManager = GPSLocationManager(activity!!).getInstance(activity!!)
                        gpsLocationManager.start(this@OrgActivity)
                    }
                })

        }
    }

    override fun updateLocation(location: Location) {

       var res:DoubleArray = GPSUtil.gps84_To_bd09(location.latitude,location.longitude)


            Toast.makeText(this,  "${location.longitude},${location.latitude}", Toast.LENGTH_SHORT).show();
    }

    override fun updateStatus(provider: String, status: Int, extras: Bundle) {
        Toast.makeText(this, "定位类型：$provider", Toast.LENGTH_SHORT).show();
    }

    override fun updateGPSProviderStatus(gpsStatus: Int) {
        TODO("Not yet implemented")
    }
}