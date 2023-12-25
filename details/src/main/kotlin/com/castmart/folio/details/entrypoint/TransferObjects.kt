package com.castmart.folio.details.entrypoint

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
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
    var status: String,
) {
    companion object {
        @JvmStatic
        fun fromGetResponse(entity: GetATicketUseCase.Response): TicketDTOV1 {
            return TicketDTOV1(
                entity.id,
                entity.ticketNumber,
                entity.shoeDescription,
                entity.ownerName,
                entity.ownerPhoneNumber,
                entity.ownerEmail,
                entity.approxCompletionDate,
                entity.status.name,
            )
        }

        @JvmStatic
        fun fromCreateResponse(entity: CreateTicketUseCase.Response): TicketDTOV1 {
            return TicketDTOV1(
                entity.id,
                entity.ticketNumber,
                entity.shoeDescription,
                entity.ownerName,
                entity.ownerPhoneNumber,
                entity.ownerEmail,
                entity.approxCompletionDate,
                entity.status.name,
            )
        }

        @JvmStatic
        fun fromUpdateResponse(entity: UpdateTicketUseCase.Response): TicketDTOV1 {
            return TicketDTOV1(
                entity.id,
                entity.ticketNumber,
                entity.shoeDescription,
                entity.ownerName,
                entity.ownerPhoneNumber,
                entity.ownerEmail,
                entity.approxCompletionDate,
                entity.status.name,
            )
        }
    }
}

data class TicketListDTOV1(val tickets: List<TicketDTOV1>)
