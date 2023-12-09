package com.castmart.api.folio.domain

import java.time.OffsetDateTime

data class Ticket(
    val ticketNumber: String,
    val shoeDescription: String,
    val ownerName: String,
    val ownerPhoneNumber: String,
    val ownerEmail: String,
    val approxCompletionDate: OffsetDateTime,
    var status: TicketStatus // TicketStatus is an enum representing different statuses
)

// Enum to represent different statuses of a ticket
enum class TicketStatus {
    IN_PROGRESS,
    READY_FOR_PICKUP,
    COMPLETED,
    // Add other possible statuses as needed
}