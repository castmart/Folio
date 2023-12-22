package com.castmart.core.usecase

import com.castmart.core.port.TicketRepository
import java.time.OffsetDateTime
import java.util.UUID

interface GetATicketUseCase {
    fun getTicket(ticketId: UUID): Response

    data class Response(
        val id: UUID,
        val ticketNumber: String,
        val shoeDescription: String,
        val ownerName: String,
        val ownerPhoneNumber: String,
        val ownerEmail: String,
        val approxCompletionDate: OffsetDateTime,
        val status: String,
    )
}

class GetATicketUseCaseImpl(private val ticketRepository: TicketRepository) : GetATicketUseCase {
    override fun getTicket(ticketId: UUID): GetATicketUseCase.Response {
        val ticket = ticketRepository.get(ticketId)

        return GetATicketUseCase.Response(
            ticket.id,
            ticket.ticketNumber,
            ticket.shoeDescription,
            ticket.ownerName,
            ticket.ownerPhoneNumber,
            ticket.ownerEmail,
            ticket.completionDate,
            ticket.status.toString(),
        )
    }
}
