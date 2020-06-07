package vn.vistark.pharmass.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

class SimpfyLocationUtils {
    companion object {
        var mLastLocation: Location? = null

        @SuppressLint("MissingPermission")
        fun getLastLocation(
            fusedLocationProviderClient: FusedLocationProviderClient
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData(fusedLocationProviderClient)
                } else {
                    mLastLocation = location
                }
            }
        }

        @SuppressLint("MissingPermission")
        fun requestNewLocationData(fusedLocationProviderClient: FusedLocationProviderClient) {
            val mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 0
            mLocationRequest.fastestInterval = 0
            mLocationRequest.numUpdates = 1

            fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
        }

        private val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                mLastLocation = locationResult.lastLocation
            }
        }
    }
}