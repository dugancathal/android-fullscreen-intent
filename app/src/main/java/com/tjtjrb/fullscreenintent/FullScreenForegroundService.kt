package com.tjtjrb.fullscreenintent

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.tjtjrb.fullscreenintent.Constants.CHANNEL_ID
import java.time.Instant

class FullScreenForegroundService : Service() {
    private val COUNTDOWN_DURATION_MILLIS = 15_000L
    override fun onBind(p0: Intent?): IBinder? = null

    private var endTime: Instant? = null
    private val handler = Handler()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        endTime = Instant.now().plusMillis(COUNTDOWN_DURATION_MILLIS)

        startForeground(1234, buildCountdownNotification())

        handler.postDelayed(::updateRemainingTime, 1000)

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
        }, COUNTDOWN_DURATION_MILLIS)

        return START_STICKY
    }

    private fun buildCountdownNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setCustomContentView(buildNotificationView(endTime))
            .setCustomBigContentView(buildNotificationView(endTime))
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Imma go foreground!!!")
            .setContentText("Get ready! ... Are you ready yet?")
            .build()
    }

    private fun buildNotificationView(endTime: Instant?): RemoteViews {
        val view = RemoteViews(packageName, R.layout.soon_to_be_foreground_notification)

        val secondsRemaining = secondsUntil(endTime)
        view.setTextViewText(
            R.id.notification_time_remaining,
            getString(R.string.time_to_foreground, secondsRemaining)
        )
        return view
    }

    private fun secondsUntil(endTime: Instant?): Long {
        val now = Instant.now().epochSecond
        val end = endTime?.epochSecond ?: now
        val secondsRemaining = end - now
        return secondsRemaining
    }

    private fun updateRemainingTime() {
        getSystemService<NotificationManager>()!!.notify(1234, buildCountdownNotification())
        if (secondsUntil(endTime) >= 0) {
            handler.postDelayed(::updateRemainingTime, 1000)
        }
    }
}