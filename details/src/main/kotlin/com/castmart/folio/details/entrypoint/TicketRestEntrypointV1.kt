package com.castmart.folio.details.entrypoint

import com.castmart.core.entity.TicketStatus
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
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
        val ticketDTO = TicketDTOV1(
            id = UUID.randomUUID(),
            ticketNumber = "00001",
            ownerPhoneNumber = "1",
            ownerName = "John Connor",
            ownerEmail = "terminator.target@gmail.com",
            completionDate = OffsetDateTime.now().plusDays(7),
            status = TicketStatus.IN_PROGRESS,
            shoeDescription = "A shoe",
        )
        return ResponseEntity.ok(ticketDTO)
    }
}
