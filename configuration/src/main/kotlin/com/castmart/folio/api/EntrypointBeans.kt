package com.castmart.folio.api

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.annotated.TicketRestEntrypointV1
import com.castmart.folio.details.entrypoint.function.TicketRestFunctionHandlerV2
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

// @Profile("mvc-annotated")
@Configuration
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

// @Profile("mvc-functional")
@Configuration
class FunctionalEntrypointBeans {
    @Bean
    fun functionalHandler(
        getUseCase: GetATicketUseCase,
        createTicketUseCase: CreateTicketUseCase,
        updateTicketUseCase: UpdateTicketUseCase,
    ): TicketRestFunctionHandlerV2 {
        return TicketRestFunctionHandlerV2(createTicketUseCase, updateTicketUseCase, getUseCase)
    }

    @Bean
    fun router(handler: TicketRestFunctionHandlerV2): RouterFunction<ServerResponse> {
        return router {
            GET("/ticket/v2/{ticketId}", handler::getTicketById)
            accept(MediaType.APPLICATION_JSON).nest {
                PUT("/ticket/v2", handler::createTicket)
                POST("/ticket/v2", handler::updateTicket)
            }
        }
    }
}
