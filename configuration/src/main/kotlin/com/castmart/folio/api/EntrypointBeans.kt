package com.castmart.folio.api

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EntrypointBeans {

    @Bean
    fun entrypoint(
        getUseCase: GetATicketUseCase,
        createTicketUseCase: CreateTicketUseCase,
        updateTicketUseCase: UpdateTicketUseCase,
    ): TicketRestEntrypointV1 {
        return TicketRestEntrypointV1(createTicketUseCase, updateTicketUseCase, getUseCase)
    }
}