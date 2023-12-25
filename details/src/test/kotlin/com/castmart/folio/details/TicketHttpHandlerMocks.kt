package com.castmart.folio.details

import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TicketHttpHandlerMocks {
    @Bean
    open fun getATicketUseCase() = mockk<GetATicketUseCase>()
    @Bean
    open fun updateTicketUseCase() = mockk<UpdateTicketUseCase>()
    @Bean
    open fun createTicketUseCase() = mockk<CreateTicketUseCase>()

    @Bean
    open fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.registerModule(JavaTimeModule())
        return objectMapper
    }
}