package com.castmart.core.entity

import java.time.OffsetDateTime
import java.util.*

data class Ticket(
    val id: UUID,
    val ticketNumber: String,
    val shoeDescription: String,
    val ownerName: String,
    val ownerPhoneNumber: String,
    val ownerEmail: String,
    val approxCompletionDate: OffsetDateTime,
    var status: TicketStatus // com.castmart.core.entity.TicketStatus is an enum representing different statuses
)

// Enum to represent different statuses of a ticket
enum class TicketStatus {
    IN_PROGRESS,
    READY_FOR_PICKUP,
    COMPLETED,
    // Add other possible statuses as needed
}