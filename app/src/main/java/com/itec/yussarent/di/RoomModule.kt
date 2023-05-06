package com.itec.yussarent.di

import com.itec.yussarent.data.repositories.RentalRepository
import com.itec.yussarent.util.INetworkErrorHandler
import com.itec.yussarent.util.NetworkErrorHandlerFactory
import com.itec.yussarent.viewModels.RoomViewModel
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
        repository: RentalRepository,
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
