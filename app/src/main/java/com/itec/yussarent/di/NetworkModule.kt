package com.itec.yussarent.di

import com.itec.yussarent.util.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides

    fun provideNetworkErrorHandlerFactory(): NetworkErrorHandlerFactory {
        return NetworkErrorHandlerFactory
    }

    @Singleton
    @Provides
    @Named("RoomListNetworkErrorHandler")
    fun provideRoomListNetworkErrorHandler(): INetworkErrorHandler {
        return RoomListNetworkErrorHandler()
    }

    @Singleton
    @Provides
    @Named("BuildingDueInvoicesNetworkErrorHandler")
    fun provideBuildingDueInvoicesNetworkErrorHandler(): INetworkErrorHandler {
        return BuildingDueInvoicesNetworkErrorHandler()
    }

    @Singleton
    @Provides
    @Named("DueInvoicesNetworkErrorHandler")
    fun provideDueInvoicesNetworkErrorHandler(): INetworkErrorHandler {
        return DueInvoicesNetworkErrorHandler()
    }
    @Singleton
    @Provides
    @Named("AvailableRoomsNetworkErrorHandler")
    fun provideAvailableRoomsNetworkErrorHandler(): INetworkErrorHandler {
        return AvailableRoomsNetworkErrorHandler()
    }
    @Singleton
    @Provides
    @Named("BuildingDuePaymentNetworkErrorHandler")
    fun provideBuildingDuePaymentNetworkErrorHandler(): INetworkErrorHandler {
        return BuildingDuePaymentNetworkErrorHandler()
    }
    @Singleton
    @Provides
    @Named("BuildingsListNetworkErrorHandler")
    fun provideBuildingsListNetworkErrorHandler(): INetworkErrorHandler {
        return BuildingsListNetworkErrorHandler()
    }

    @Singleton
    @Provides
    @Named("OccupiedRoomsNetworkErrorHandler")
    fun provideOccupiedRoomsNetworkErrorHandler(): INetworkErrorHandler {
        return OccupiedRoomsNetworkErrorHandler()
    }

    @Provides
    @Singleton
    fun provideNetworkErrorHandlers(
        roomListNetworkErrorHandler: INetworkErrorHandler,
        buildingDueInvoicesNetworkErrorHandler: INetworkErrorHandler,
        dueInvoicesNetworkErrorHandler: INetworkErrorHandler,
        availableRoomsNetworkErrorHandler: INetworkErrorHandler,
        buildingDuePaymentNetworkErrorHandler: INetworkErrorHandler,
        buildingsListNetworkErrorHandler: INetworkErrorHandler,
        occupiedRoomsNetworkErrorHandler: INetworkErrorHandler
    ): Map<String, @JvmSuppressWildcards INetworkErrorHandler> {
        return mapOf(
            "RoomListNetworkErrorHandler" to roomListNetworkErrorHandler,
            "BuildingDueInvoicesNetworkErrorHandler" to buildingDueInvoicesNetworkErrorHandler,
            "DueInvoicesNetworkErrorHandler" to dueInvoicesNetworkErrorHandler,
            "AvailableRoomsNetworkErrorHandler" to availableRoomsNetworkErrorHandler,
            "BuildingDuePaymentNetworkErrorHandler" to buildingDuePaymentNetworkErrorHandler,
            "BuildingsListNetworkErrorHandler" to buildingsListNetworkErrorHandler,
            "OccupiedRoomsNetworkErrorHandler" to occupiedRoomsNetworkErrorHandler
        )
    }


}
