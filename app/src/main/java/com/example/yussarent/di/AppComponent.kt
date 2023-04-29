package com.example.yussarent.di

import com.example.yussarent.work.CustomWorkerFactory
import com.example.yussarent.ui.views.LoginActivity
import com.example.yussarent.ui.views.MainActivity
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: LoginActivity)

    fun workerFactory(): CustomWorkerFactory
}

