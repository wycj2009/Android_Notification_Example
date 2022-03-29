package com.example.android_notification_example

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_notification_example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_GROUP_KEY = "notification_group_key"
        var notificationID = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        sendNotificationGroupSummary()

        binding.button.setOnClickListener {
            sendNotification()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "description"
            enableVibration(true)
            vibrationPattern = longArrayOf(200, 200)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotificationGroupSummary() {
        val notification = Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setSubText("sub_text")
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setGroupSummary(true)
                .build()

        notificationManager.notify(notificationID, notification)
    }

    private fun sendNotification() {
        notificationID++

        val openPendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java).apply { putExtra("notification_id", notificationID) },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val dismissPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, DismissNotiReceiver::class.java).apply { putExtra("notification_id", notificationID) },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val action = Notification.Action.Builder(
                Icon.createWithResource(this, android.R.drawable.ic_dialog_info),
                "Dismiss",
                dismissPendingIntent
        ).build()

        val notification = Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("content_title")
                .setContentText("notification_id - $notificationID")
                .setContentIntent(openPendingIntent)
                .setAutoCancel(true)
                .setActions(action)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .build()

        notificationManager.notify(notificationID, notification)
    }

}