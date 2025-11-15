package com.example.estiamapp.work

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

// Fonction pour planifier une tâche unique après 10 secondes
fun enqueueOneTimeAfter10Sec(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
        .setInitialDelay(10, TimeUnit.SECONDS)
        .addTag("one_time_work")
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

// Fonction pour planifier une tâche avec contraintes (Wi-Fi + Charging)
fun enqueueOneTimeWifiCharging(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi uniquement
        .setRequiresCharging(true) // En charge seulement
        .build()

    val workRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
        .setConstraints(constraints)
        .addTag("constrained_work")
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}