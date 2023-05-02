package com.example.yussarent.di

import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.example.yussarent.data.repositories.impl.UserRepositoryImpl
import com.example.yussarent.network.ApiService
import com.example.yussarent.viewModels.LoginViewModel
import com.example.yussarent.viewModels.RoomViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRentalRepository(apiService: ApiService): RentalRepositoryImpl {
        return RentalRepositoryImpl(apiService)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    @Provides
    fun provideUserRepository(apiService: ApiService): UserRepositoryImpl {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
             .baseUrl("https://yyussa.xode.rw/landlord/api/management.php/")
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(ApiService::class.java)
    }

@Provides
@Singleton
fun provideLoginViewModel(userRepository: UserRepositoryImpl): LoginViewModel {
    return LoginViewModel(userRepository)
}
}




