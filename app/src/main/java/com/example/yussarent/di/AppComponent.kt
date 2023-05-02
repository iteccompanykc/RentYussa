package com.example.yussarent.di

import com.example.yussarent.work.CustomWorkerFactory
import com.example.yussarent.ui.views.LoginActivity
import com.example.yussarent.ui.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class,RoomModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(activity: LoginActivity)

    fun workerFactory(): CustomWorkerFactory
}


