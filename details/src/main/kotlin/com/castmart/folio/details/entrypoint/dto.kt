package com.castmart.folio.details.entrypoint

import com.castmart.core.entity.TicketStatus
import java.time.OffsetDateTime
import java.util.UUID

data class TicketDTOV1(
    val id: UUID,
    val ticketNumber: String,
    val shoeDescription: String,
    val ownerName: String,
    val ownerPhoneNumber: String,
    val ownerEmail: String,
    val completionDate: OffsetDateTime,
    var status: TicketStatus,
)

data class TicketListDTOV1(val tickets: List<TicketDTOV1>)
