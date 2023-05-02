package com.example.yussarent.di

import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.example.yussarent.util.INetworkErrorHandler
import com.example.yussarent.util.NetworkErrorHandlerFactory
import com.example.yussarent.viewModels.RoomViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ActivityComponent::class)
object RoomModule {
    @Provides
    fun provideRoomViewModel(
        repository: RentalRepositoryImpl,
        coroutineScope: CoroutineScope,
        networkErrorHandlerFactory: NetworkErrorHandlerFactory,
        roomListNetworkErrorHandler: INetworkErrorHandler,
        buildingDueInvoicesNetworkErrorHandler: INetworkErrorHandler,
        dueInvoicesNetworkErrorHandler: INetworkErrorHandler,
        availableRoomsNetworkErrorHandler: INetworkErrorHandler,
        buildingDuePaymentNetworkErrorHandler: INetworkErrorHandler,
        buildingsListNetworkErrorHandler: INetworkErrorHandler,
        occupiedRoomsNetworkErrorHandler: INetworkErrorHandler,

    ): RoomViewModel {
        return RoomViewModel(
            repository,
            coroutineScope,
            networkErrorHandlerFactory,
            roomListNetworkErrorHandler,
            buildingDueInvoicesNetworkErrorHandler,
            dueInvoicesNetworkErrorHandler,
            availableRoomsNetworkErrorHandler,
            buildingDuePaymentNetworkErrorHandler,
            buildingsListNetworkErrorHandler,
            occupiedRoomsNetworkErrorHandler
        )
    }
}
