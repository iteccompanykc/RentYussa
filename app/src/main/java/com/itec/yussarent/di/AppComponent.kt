package com.itec.yussarent.di
import com.itec.yussarent.ui.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class,LoginViewModelModule::class, RoomModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

}

