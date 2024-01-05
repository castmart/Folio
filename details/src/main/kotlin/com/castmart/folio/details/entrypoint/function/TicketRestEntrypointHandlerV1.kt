package com.castmart.folio.details.entrypoint.function

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.TicketDTOV1
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.notFound
import org.springframework.web.servlet.function.ServerResponse.ok
import java.util.UUID

class TicketRestEntrypointHandlerV1(
    private val createTicketUseCase: CreateTicketUseCase,
    private val updateTicketUseCase: UpdateTicketUseCase,
    private val getATicketUseCase: GetATicketUseCase,
) {
    fun getTicketById(request: ServerRequest): ServerResponse {
        val ticketId = UUID.fromString(request.pathVariable("ticketId"))
        return getATicketUseCase.getTicket(ticketId).let {
            ok().contentType(MediaType.APPLICATION_JSON).body(TicketDTOV1.fromGetResponse(it))
        } ?: notFound().build()
    }

    fun createTicket(request: ServerRequest): ServerResponse {
        val dtoV1 = request.body(TicketDTOV1::class.java)
        val createResponse =
            createTicketUseCase.createTicket(
                CreateTicketUseCase.Request(
                    ticketNumber = dtoV1.ticketNumber,
                    ownerName = dtoV1.ownerName,
                    ownerEmail = dtoV1.ownerEmail,
                    ownerPhoneNumber = dtoV1.ownerPhoneNumber,
                    shoeDescription = dtoV1.shoeDescription,
                    approxCompletionDate = dtoV1.completionDate,
                ),
            )

        return createResponse.let {
            ok().contentType(MediaType.APPLICATION_JSON).body(TicketDTOV1.fromCreateResponse(it))
        }
    }

    fun updateTicket(request: ServerRequest): ServerResponse {
        val dtoV1 = request.body(TicketDTOV1::class.java)
        val updateResponse =
            updateTicketUseCase.updateTicket(
                ticketRequest =
                    UpdateTicketUseCase.Request(
                        id = dtoV1.id,
                        ticketNumber = dtoV1.ticketNumber,
                        ownerName = dtoV1.ownerName,
                        ownerEmail = dtoV1.ownerEmail,
                        ownerPhoneNumber = dtoV1.ownerPhoneNumber,
                        shoeDescription = dtoV1.shoeDescription,
                        approxCompletionDate = dtoV1.completionDate,
                        updateStatus = dtoV1.status,
                    ),
            )

        return updateResponse.let {
            ok().contentType(MediaType.APPLICATION_JSON).body(TicketDTOV1.fromUpdateResponse(it))
        }
    }
}
