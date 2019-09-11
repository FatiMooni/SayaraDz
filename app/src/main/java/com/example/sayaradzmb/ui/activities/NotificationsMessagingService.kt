package com.example.sayaradzmb.ui.activities

import android.util.Log

import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.app.Notification
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.sayaradzmb.R
import com.example.sayaradzmb.constatnte.GROUP_KEY_WORK_EMAIL
import java.io.IOException
import java.net.URL


class NotificationsMessagingService : MessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var data = remoteMessage.data
        var notification = remoteMessage.notification
        Log.i("NotificationsService", remoteMessage.toString())
        Log.i("NotificationsService", data.toString())

        afficherNotification(notification!!,data)

    }

    private fun afficherNotification(
        notification: RemoteMessage.Notification,
        data: Map<String,String>
    ) {
        val icon = BitmapFactory.decodeResource(resources, R.drawable.logo)

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            .setContentInfo(notification.title)
            .setLargeIcon(icon)
            .setColor(Color.RED)
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.logo)
            .setGroup(GROUP_KEY_WORK_EMAIL)

        try {
            val picture_url = data["CheminImage"]
            if (picture_url != null && "" != picture_url) {
                val url = URL(picture_url)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(
                        notification.body
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}