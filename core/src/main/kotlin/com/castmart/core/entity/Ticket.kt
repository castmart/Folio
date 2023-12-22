package com.castmart.core.entity

import java.time.OffsetDateTime
import java.util.UUID

data class Ticket(
    val id: UUID,
    val ticketNumber: String,
    val shoeDescription: String,
    val ownerName: String,
    val ownerPhoneNumber: String,
    val ownerEmail: String,
    val completionDate: OffsetDateTime,
    var status: TicketStatus,
)

// Enum to represent different statuses of a ticket
enum class TicketStatus {
    IN_PROGRESS,
    READY_FOR_PICKUP,
    COMPLETED,
    // Add other possible statuses as needed
}
