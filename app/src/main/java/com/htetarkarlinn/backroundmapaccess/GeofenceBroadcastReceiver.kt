package com.htetarkarlinn.backroundmapaccess

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.maps.model.LatLng

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show()
        val notificationHelper : NotificationHelper=NotificationHelper(context)
        val geofencingEvent : GeofencingEvent= GeofencingEvent.fromIntent(intent)!!
        val con : Class<MapsActivity> =MapsActivity::class.java
        if (geofencingEvent.hasError()){
            Log.d("TAG", "onReceive: Error Receiving geofence event ...")
            return
        }
        val geofenceList : List<Geofence> = geofencingEvent.triggeringGeofences!!
        for (geofence in geofenceList){
            Log.d("TAG", "onReceive: "+geofence.requestId)
            //Toast.makeText(context, "${geofenceList.size}onReceive"+geofence.latitude, Toast.LENGTH_SHORT).show()
        }
        val transitionType =geofencingEvent.geofenceTransition
        val position : LatLng=LatLng(geofenceList.last().latitude,geofenceList.last().longitude)
        //Toast.makeText(context, "${geofencingEvent.triggeringLocation}", Toast.LENGTH_SHORT).show()
        val ywama = LatLng(16.834680, 96.124014)
        val itVisionHub=LatLng(16.82521999076325, 96.1258090411045)
        var locationName=""
        var discount=0
        when (position) {
            ywama -> {
               locationName= "Ywama Kyaung"
               discount=0
            }
            itVisionHub -> {
                locationName="itVisionHub"
                discount=10
            }
            else -> {
                locationName="Clicked location"
            }
        }



        when(transitionType){

            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER to $locationName", Toast.LENGTH_SHORT)
                    .show()
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER","You are near the $locationName and this shop have $discount % discount" ,con)
            }
            Geofence.GEOFENCE_TRANSITION_DWELL ->{
                //Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL in $locationName", Toast.LENGTH_SHORT)
                //.show()
               // notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL","You are dwell in $locationName",con)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                //Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT from $locationName", Toast.LENGTH_SHORT).show()
                //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT","You are exit from $locationName",con)
            }

        }
    }
}