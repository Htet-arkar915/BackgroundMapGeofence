package com.htetarkarlinn.backroundmapaccess

import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.htetarkarlinn.backroundmapaccess.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper
    var geofenceId="Some_Geofence_Id"
    val fineRequestCode=101
    val backgroundRequestCode=102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        geofencingClient=LocationServices.getGeofencingClient(this)
        geofenceHelper= GeofenceHelper(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val ywama = LatLng(16.834680, 96.124014)
        mMap.addMarker(MarkerOptions().position(ywama).title("Marker in Ywama kyaung street"))
        val itVisionHub=LatLng(16.82521999076325, 96.1258090411045)
        mMap.addMarker(MarkerOptions().position(itVisionHub).title("ITVisionHub"))
        addCircle(ywama,500.0)
        addCircle(itVisionHub,500.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(itVisionHub))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ywama))
        enableUserLocation()
        mMap.setOnMapLongClickListener(this)
        addGeofence(ywama,500.0.toFloat())
        addGeofence(itVisionHub,500.0.toFloat())

    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled=true
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Please Open Location", Toast.LENGTH_SHORT).show()
                val arr : Array<String> = arrayOf(ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this,arr,100)
            }else{
                val arr : Array<String> = arrayOf(ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this,arr,100)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==fineRequestCode){
            if (grantResults.isNotEmpty()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap.isMyLocationEnabled=true
            }else{
                Toast.makeText(this, "Do not have permission", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode==backgroundRequestCode){
            if (grantResults.isNotEmpty()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                Toast.makeText(this, "You can add geofence", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Background location access is necessary for geofence to trigger", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addMarker(latLng: LatLng){
       val  markerOption : MarkerOptions=MarkerOptions().position(latLng)
        mMap.addMarker(markerOption)

    }
    private fun addCircle(latLng: LatLng,radius : Double){
        val circleOptions : CircleOptions= CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius)
        circleOptions.strokeColor(Color.argb(255,255,0,0))
        circleOptions.fillColor(Color.argb(64,255,0,0))
        circleOptions.strokeWidth(4F)
        mMap.addCircle(circleOptions)


    }

    override fun onMapLongClick(p: LatLng) {
        //val notificationHelper : NotificationHelper= NotificationHelper(this)
       // notificationHelper.sendHighPriorityNotification("Click","On longClick listener",MapsActivity::class.java)

        if (Build.VERSION.SDK_INT>=29){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)== PackageManager.PERMISSION_GRANTED){
                handleMapLongClick(p)
            }else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    val arr : Array<String> = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    ActivityCompat.requestPermissions(this,arr ,backgroundRequestCode)
                }else{
                    val arr : Array<String> = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    ActivityCompat.requestPermissions(this,arr ,backgroundRequestCode)
                }
            }
        }else{
            handleMapLongClick(p)
        }
    }
    private fun handleMapLongClick(latLng: LatLng){
        //mMap.clear()
        //Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        addMarker(latLng)
        addCircle(latLng,500.0)
        addGeofence(latLng,500.0.toFloat())
    }
    private fun addGeofence(latLng: LatLng,radius: Float){
        val geofence : Geofence=geofenceHelper.getGeofence(geofenceId,latLng,radius,Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT)
        val geofencingRequest : GeofencingRequest=geofenceHelper.geofencingRequest(geofence)
        val pendingIntent : PendingIntent=geofenceHelper.getPendingIntent()
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(geofencingRequest,pendingIntent)
            .addOnSuccessListener {
                Log.d("Listener", "addGeofenc:onSuccess ...${geofence.latitude}" )
                //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}