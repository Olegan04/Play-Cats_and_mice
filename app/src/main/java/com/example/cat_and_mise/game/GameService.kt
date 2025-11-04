package com.example.cat_and_mise.game

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.cat_and_mise.R

class GameService : Service() {

    companion object {
        private const val CHANNEL_ID = "game_channel"
        private const val NOTIFICATION_ID = 1

        fun start(context: Context, score: Int) {
            val intent = Intent(context, GameService::class.java).apply {
                putExtra("score", score)
            }
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, GameService::class.java)
            context.stopService(intent)
        }

        fun updateNotification(context: Context, score: Int) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = createNotification(context, score)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

        private fun createNotification(context: Context, score: Int): Notification {
            createNotificationChannel(context)

            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Кошки-Мышки")
                .setContentText("Текущий счет: $score")
                .setSmallIcon(R.drawable.ic_game)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build()
        }

        private fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Игровые уведомления",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Показывает текущий счет в игре"
                }

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val score = intent?.getIntExtra("score", 0) ?: 0
        val notification = createNotification(this, score)
        startForeground(NOTIFICATION_ID, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }
}