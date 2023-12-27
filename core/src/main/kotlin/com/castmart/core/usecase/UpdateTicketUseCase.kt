package com.castmart.core.usecase

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import java.time.OffsetDateTime
import java.util.UUID

interface UpdateTicketUseCase {
    fun updateTicket(ticketRequest: Request): Response

    data class Request(
        val id: UUID,
        val ticketNumber: String,
        val shoeDescription: String,
        val ownerName: String,
        val ownerPhoneNumber: String,
        val ownerEmail: String,
        val approxCompletionDate: OffsetDateTime,
        val updateStatus: String,
    )

    data class Response(
        val id: UUID,
        val ticketNumber: String,
        val shoeDescription: String,
        val ownerName: String,
        val ownerPhoneNumber: String,
        val ownerEmail: String,
        val approxCompletionDate: OffsetDateTime,
        val status: TicketStatus,
    )
}

class UpdateTicketUseCaseImpl(private val ticketRepository: TicketRepository) : UpdateTicketUseCase {
    override fun updateTicket(ticketRequest: UpdateTicketUseCase.Request): UpdateTicketUseCase.Response {
        val originalTicket = ticketRepository.get(ticketRequest.id)

        val ticketToEdit =
            Ticket(
                id = ticketRequest.id,
                ticketNumber = ticketRequest.ticketNumber,
                shoeDescription = ticketRequest.shoeDescription,
                ownerName = ticketRequest.ownerName,
                ownerEmail = ticketRequest.ownerEmail,
                ownerPhoneNumber = ticketRequest.ownerPhoneNumber,
                completionDate =
                    validateOriginalIsOlder(
                        originalTicket.completionDate,
                        ticketRequest.approxCompletionDate,
                    ),
                // Validate it is in the future if not finished yet
                status = TicketStatus.valueOf(ticketRequest.updateStatus),
            )
        val updatedTicket = ticketRepository.update(ticketToEdit)
        return UpdateTicketUseCase.Response(
            id = updatedTicket.id,
            ticketNumber = updatedTicket.ticketNumber,
            shoeDescription = updatedTicket.shoeDescription,
            ownerName = updatedTicket.ownerName,
            ownerPhoneNumber = updatedTicket.ownerPhoneNumber,
            ownerEmail = updatedTicket.ownerEmail,
            approxCompletionDate = updatedTicket.completionDate,
            status = updatedTicket.status,
        )
    }

    private fun validateOriginalIsOlder(
        original: OffsetDateTime,
        toBeSet: OffsetDateTime,
    ): OffsetDateTime {
        return if (original.isBefore(toBeSet)) {
            toBeSet
        } else {
            throw IllegalArgumentException("Invalid Date: New Date is older.")
        }
    }
}
