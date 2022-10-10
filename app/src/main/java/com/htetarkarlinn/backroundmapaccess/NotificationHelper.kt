package com.htetarkarlinn.backroundmapaccess

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Random

class NotificationHelper(base: Context) : ContextWrapper(base) {

    private val channelName: String ="High priority channel"
    private val channelId= "com.example.notifications$channelName"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val notificationChannel : NotificationChannel=
            NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.description="This is description"
        notificationChannel.lightColor=Color.RED
        notificationChannel.lockscreenVisibility=Notification.VISIBILITY_PUBLIC
        val notManager :NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notManager.createNotificationChannel(notificationChannel)
    }
     @SuppressLint("UnspecifiedImmutableFlag")
     fun sendHighPriorityNotification(title: String, body: String, MapsActivity: Class<MapsActivity>){
        val intent = Intent(this,MapsActivity)
        val pendingIntent : PendingIntent=PendingIntent.getActivity(this,267,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val notification :Notification=NotificationCompat.Builder(this,channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.map)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().setSummaryText("Summary").setBigContentTitle(title).bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(this).notify(Random().nextInt(),notification)
    }

}