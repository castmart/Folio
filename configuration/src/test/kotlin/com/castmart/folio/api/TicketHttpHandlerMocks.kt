package com.castmart.folio.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TicketHttpHandlerMocks {
//    @Bean
//    open fun getATicketUseCase(): GetATicketUseCase {
//        return mockk<GetATicketUseCase>()
//    }
//    @Bean
//    open fun updateTicketUseCase() = mockk<UpdateTicketUseCase>()
//    @Bean
//    open fun createTicketUseCase() = mockk<CreateTicketUseCase>()

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.registerModule(JavaTimeModule())
        return objectMapper
    }
}
