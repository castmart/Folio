package com.castmart.folio.details.entrypoint

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import java.time.OffsetDateTime
import java.util.UUID

// {"id":"93BB0038-CB9D-433F-B830-FA627DE32F76", "ticketNumber":"1", "ownerName": "Juan", "ownerEmail":"email", "ownerPhoneNumber": "01", "shoeDescription": "A shoe", "completionDate": "2023-12-31 00:00:00", "status": "IN_PROGRESS" }
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

data class ErrorDto(val statusCode: Int, val message: String)
