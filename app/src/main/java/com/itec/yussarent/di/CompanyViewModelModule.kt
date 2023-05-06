package com.itec.yussarent.di

import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.viewModels.CompanyViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ActivityComponent::class)
object CompanyViewModelModule {
    @Provides
    fun provideLoginViewModel(userRepository: UserRepository, coroutineScope: CoroutineScope,): CompanyViewModel {
        return CompanyViewModel(userRepository,coroutineScope)
    }
}