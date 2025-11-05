package com.example.estiamapp

import android.app.Application
import com.example.estiamapp.notifications.NotificationHelper

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createChannel(this)
    }
}