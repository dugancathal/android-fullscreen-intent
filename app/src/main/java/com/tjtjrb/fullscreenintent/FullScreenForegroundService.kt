package com.tjtjrb.fullscreenintent

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.tjtjrb.fullscreenintent.Constants.CHANNEL_ID

class FullScreenForegroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1234, NotificationCompat.Builder(this, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Imma go foreground!!!")
            .setContentText("Get ready! ... Are you ready yet?")
            .build())

        val handler = Handler()
        handler.postDelayed({
            val activityIntent = Intent(this, MainActivity::class.java).apply {
                data = Uri.parse("fullscreenapp://im-from-a-service/From-Service")
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("I'm foregrounded!!!!")
                .setContentText("Look at me all in the foreground and stuff.")
                .setFullScreenIntent(pendingIntent, true)
                .build()
            getSystemService<NotificationManager>()!!.notify(1235, notification)

            handler.postDelayed({
                stopSelf()
            }, 5000)
        }, 10_000)

        return START_STICKY
    }
}