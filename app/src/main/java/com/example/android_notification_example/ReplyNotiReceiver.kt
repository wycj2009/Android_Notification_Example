package com.example.android_notification_example

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReplyNotiReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        RemoteInput.getResultsFromIntent(intent)?.run {
            Toast.makeText(context, getCharSequence("text_reply_key").toString(), Toast.LENGTH_SHORT).show()
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(intent.getIntExtra("notification_id", -1))
    }

}