package com.adharul.i.githubusers.model.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.view.activity.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val CONTENT_TITLE = "Github App"
        private const val EXTRA_MESSAGE = "message"
        private const val ID_NOTIF_ALARM = 101
        private const val MILLIS_24_HOUR = 86400000
        private const val CHANNEL_ID = "Channel_1"
        private const val CHANNEL_NAME = "AlarmManager channel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        showAlarmNotification(context, message)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, context.getString(R.string.notification_message))
        val pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIF_ALARM, intent, 0)

        //Di set pada jam 9 pagi
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
        }

        //agar nilai parameter triggerAtMillis pada fungsi setInExactRepeating lebih besar dari System.currentTimeMillis()
        //Karena jika triggerAtMillis lebih kecil dari System.currentTimeMillis() maka onReceive() akan langsung ter trigger
        val targetTime = if(calendar.timeInMillis < System.currentTimeMillis()){
            //(millis sekarang + 24 jam) - (millis sekarang - calendar)
            (System.currentTimeMillis() + MILLIS_24_HOUR) - (System.currentTimeMillis() - calendar.timeInMillis)
        } else{
            calendar.timeInMillis
        }

        //Log.i("TIME", "$targetTime & ${System.currentTimeMillis()}")

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            targetTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIF_ALARM, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun showAlarmNotification(context: Context, message: String?) {
        val notifDetailIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notifDetailIntent, 0)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications_white_24dp)
            .setContentTitle(CONTENT_TITLE)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_NOTIF_ALARM, notification)
    }
}
