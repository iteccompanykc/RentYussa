package com.example.yussarent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yussarent.viewModels.LoginViewModel
import com.example.yussarent.work.ViewModelFactory
import com.example.yussarent.work.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class ViewModelModule {
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel
//
//    @Binds
//    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
//}
