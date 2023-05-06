package com.itec.yussarent.di

import com.itec.yussarent.data.repositories.RentalRepository
import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.itec.yussarent.data.repositories.impl.UserRepositoryImpl
import com.itec.yussarent.network.ApiService
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
    fun provideRentalRepository(apiService: ApiService): RentalRepository {
        return RentalRepositoryImpl(apiService)
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
             .baseUrl("https://yyussa.xode.rw/landlord/api/management.php/")
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(ApiService::class.java)
    }


}




