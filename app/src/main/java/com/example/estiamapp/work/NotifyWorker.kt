package com.example.estiamapp.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.estiamapp.notifications.NotificationHelper

class NotifyWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("NotifyWorker", "Worker started")

            val title = inputData.getString("title") ?: "Tâche exécutée!"
            val message = inputData.getString("message") ?: "WorkManager a terminé une tâche en arrière-plan"

            val success = NotificationHelper.show(
                context = applicationContext,
                title = title,
                message = message,
                notificationId = 2
            )

            if (success) {
                Log.i("NotifyWorker", "Notification sent successfully")
                Result.success()
            } else {
                Log.w("NotifyWorker", "Failed to send notification")
                Result.failure()
            }

        } catch (e: Exception) {
            Log.e("NotifyWorker", "Worker failed", e)
            Result.failure()
        }
    }
}