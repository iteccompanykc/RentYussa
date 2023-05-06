package com.itec.yussarent.di

import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.viewModels.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object LoginViewModelModule {
    @Provides
    fun provideLoginViewModel(userRepository: UserRepository): LoginViewModel {
        return LoginViewModel(userRepository)
    }
}