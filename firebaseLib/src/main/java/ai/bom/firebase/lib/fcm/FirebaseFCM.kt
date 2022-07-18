package ai.bom.firebase.lib.fcm

import ai.bom.firebase.lib.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

@Keep
class FirebaseFCM(private val context: Context) {

    fun initializeFCM(topic: String) {
        try {
            FirebaseApp.initializeApp(context)
        } catch (e: Exception) {
            Log.i(TAG, "onCreate: ${e.message}")
        }
        createNotificationChannel(context)
        //Subscribe To Topic
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    fun removeFCMTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.getString(R.string.default_notification_channel_id)
            val channelName = context.getString(R.string.default_notification_channel_name)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}