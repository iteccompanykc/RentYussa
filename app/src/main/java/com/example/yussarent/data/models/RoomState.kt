package com.example.yussarent.data.models

data class RoomState(
    val rooms: List<Room>,
    val availableRooms: List<Room>,
    val occupiedRooms: List<Room>,
    val dueInvoices: List<Invoice>,
    val duePayments: List<Payment>
)

