package com.xiaoxin.basic.location

import android.location.Location
import android.location.LocationListener
import android.location.LocationProvider
import android.os.Bundle

class GPSLocation(gpsLocationListener: GPSLocationListener) : LocationListener {

    var mGPSLocationListener: GPSLocationListener? = gpsLocationListener

    override fun onLocationChanged(location: Location) {
        mGPSLocationListener?.updateLocation(location = location)
    }

    override fun onLocationChanged(locations: MutableList<Location>) {
        super.onLocationChanged(locations)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        mGPSLocationListener?.updateStatus(provider = provider, status = status, extras = extras)
        when (status) {
            LocationProvider.AVAILABLE -> {
                mGPSLocationListener?.updateGPSProviderStatus(GPSProviderStatus.GPS_AVAILABLE)
            }
            LocationProvider.OUT_OF_SERVICE -> {
                mGPSLocationListener?.updateGPSProviderStatus(GPSProviderStatus.GPS_OUT_OF_SERVICE)
            }
            LocationProvider.TEMPORARILY_UNAVAILABLE -> {
                mGPSLocationListener?.updateGPSProviderStatus(GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE)
            }
        }

    }


}