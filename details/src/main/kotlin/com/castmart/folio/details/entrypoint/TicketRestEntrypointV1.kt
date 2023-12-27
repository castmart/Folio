package com.castmart.folio.details.entrypoint

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/ticket/v1")
class TicketRestEntrypointV1(
    private val createTicketUseCase: CreateTicketUseCase,
    private val updateTicketUseCase: UpdateTicketUseCase,
    private val getATicketUseCase: GetATicketUseCase,
) {
    @GetMapping("/{ticketId}")
    fun getTicketById(
        @PathVariable ticketId: UUID,
    ): ResponseEntity<TicketDTOV1> {
        val ticket = getATicketUseCase.getTicket(ticketId)
        return ResponseEntity.ok(TicketDTOV1.fromGetResponse(ticket))
    }

    @PutMapping(consumes = ["application/json"], produces = ["application/json"])
    fun createTicket(
        @RequestBody dtoV1: TicketDTOV1,
    ): ResponseEntity<TicketDTOV1> {
        val createResponse = createTicketUseCase.createTicket(
                CreateTicketUseCase.Request(
                    ticketNumber = dtoV1.ticketNumber,
                    ownerName = dtoV1.ownerName,
                    ownerEmail = dtoV1.ownerEmail,
                    ownerPhoneNumber = dtoV1.ownerPhoneNumber,
                    shoeDescription = dtoV1.shoeDescription,
                    approxCompletionDate = dtoV1.completionDate,
                ),
            )
        return ResponseEntity.ok(
            TicketDTOV1.fromCreateResponse(createResponse),
        )
    }

    @PostMapping
    fun updateTicket(
        @RequestBody dtoV1: TicketDTOV1,
    ): ResponseEntity<TicketDTOV1> {
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

        return ResponseEntity.ok(
            TicketDTOV1.fromUpdateResponse(updateResponse),
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun noSuchElementException(): ResponseEntity<ErrorDto> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ErrorDto> {
        return ResponseEntity.badRequest().body(
            ErrorDto(
                HttpStatus.BAD_REQUEST.value(), exception.message.orEmpty()
            )
        )
    }
}
