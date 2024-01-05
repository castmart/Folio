package com.castmart.folio.api

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import com.castmart.folio.details.entrypoint.function.TicketRestEntrypointHandlerV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Configuration
//@Profile("mvc-annotated")
class AnnotatedEntrypointBeans {
    @Bean
    fun entrypoint(
        getUseCase: GetATicketUseCase,
        createTicketUseCase: CreateTicketUseCase,
        updateTicketUseCase: UpdateTicketUseCase,
    ): TicketRestEntrypointV1 {
        return TicketRestEntrypointV1(createTicketUseCase, updateTicketUseCase, getUseCase)
    }
}

@Configuration
//@Profile("mvc-functional")
class FunctionalEntrypointBeans {

    @Bean
    fun functionalHandler(
        getUseCase: GetATicketUseCase,
        createTicketUseCase: CreateTicketUseCase,
        updateTicketUseCase: UpdateTicketUseCase,
    ): TicketRestEntrypointHandlerV1 {
        return TicketRestEntrypointHandlerV1(createTicketUseCase, updateTicketUseCase, getUseCase)
    }

    @Bean
    fun router(handler: TicketRestEntrypointHandlerV1): RouterFunction<ServerResponse> {
        return router {
            GET("/fun/ticket/v1/{ticketId}", handler::getTicketById)
            accept(MediaType.APPLICATION_JSON).nest {
                PUT("/fun/ticket/v1", handler::createTicket)
                POST("/fun/ticket/v1", handler::createTicket)
            }
        }
    }
}