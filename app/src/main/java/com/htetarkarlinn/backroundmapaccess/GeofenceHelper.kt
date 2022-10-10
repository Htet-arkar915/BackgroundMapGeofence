package com.htetarkarlinn.backroundmapaccess

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class GeofenceHelper(base: Context?) : ContextWrapper(base) {

    private val tag : String="GeofenceHelper"
    private lateinit var  pendingIntent : PendingIntent

    fun geofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }
    fun getGeofence(ID : String,latLng: LatLng,radius : Float,transitionType : Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude,latLng.longitude,radius)
            .setRequestId(ID)
            .setTransitionTypes(transitionType)
            .setLoiteringDelay(5000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun getPendingIntent() : PendingIntent{
        val intent =Intent(this,GeofenceBroadcastReceiver::class.java)
        pendingIntent=PendingIntent.getBroadcast(this,2607,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}