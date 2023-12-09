package com.castmart.core.usecase

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import java.time.OffsetDateTime
import java.util.UUID

interface CreateTicketUseCase {

    fun createTicket(request: CreateTicketUseCase.Request): CreateTicketUseCase.Response

    data class Request(
        val ticketNumber: String,
        val shoeDescription: String,
        val ownerName: String,
        val ownerPhoneNumber: String,
        val ownerEmail: String,
        val approxCompletionDate: OffsetDateTime
    )
    data class Response(
        val id: UUID,
        val ticketNumber: String,
        val approxCompletionDate: OffsetDateTime
    )
}

class CreateTicketUseCaseImpl(
    private val repository: TicketRepository
    ): CreateTicketUseCase {

    override fun createTicket(request: CreateTicketUseCase.Request): CreateTicketUseCase.Response {
        // Validate input data.
        val ticket = Ticket(
            id = UUID.randomUUID(),
            ticketNumber = request.ticketNumber,
            shoeDescription = request.shoeDescription,
            ownerName = request.ownerName,
            ownerPhoneNumber = request.ownerPhoneNumber,
            ownerEmail = request.ownerEmail,
            approxCompletionDate = request.approxCompletionDate,
            status = TicketStatus.IN_PROGRESS
        )
        // Save in the repository
        val dbResponse = this.repository.save(ticket)
        // Return response
        return CreateTicketUseCase.Response(
            id = dbResponse.id,
            ticketNumber = dbResponse.ticketNumber,
            approxCompletionDate = dbResponse.approxCompletionDate
        )
    }

}
