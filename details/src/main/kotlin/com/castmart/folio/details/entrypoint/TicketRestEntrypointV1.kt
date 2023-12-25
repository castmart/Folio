package com.castmart.folio.details.entrypoint

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TicketRestEntrypoint(
    private val createTicketUseCase: CreateTicketUseCase,
    private val updateTicketUseCase: UpdateTicketUseCase,
    private val getATicketUseCase: GetATicketUseCase,
) {

    @GetMapping
    
}
