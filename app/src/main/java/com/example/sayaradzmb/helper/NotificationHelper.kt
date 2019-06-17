package com.example.sayaradzmb.helper

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.sayaradzmb.R
import com.example.sayaradzmb.CHANELLE_ID
import com.example.sayaradzmb.GROUP_KEY_WORK_EMAIL
import com.example.sayaradzmb.NOTIFICATION_ID

interface NotificationHelper {

    fun afficherNotification(context : Context){

        val builder = NotificationCompat.Builder(context, CHANELLE_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("MyNotification")
            .setContentText("HelloWorld")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("much longer text here"))
            .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.image_entete))
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.image_entete)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.logo,"Ouvrire",null)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setGroup(GROUP_KEY_WORK_EMAIL)


        with(NotificationManagerCompat.from(context)){
            notify(NOTIFICATION_ID,builder.build())
        }
    }
}