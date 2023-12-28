package com.castmart.folio.api

import com.castmart.core.port.TicketRepository
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.CreateTicketUseCaseImpl
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.GetATicketUseCaseImpl
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCaseImpl
import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import com.castmart.folio.details.repository.JdbcTemplateTicketRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

@Configuration
class UseCaseBeans {
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerKotlinModule()
        return objectMapper
    }

    @Bean
    fun springJdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }

    @Bean
    fun jdbcTicketRepository(jdbcTemplate: NamedParameterJdbcTemplate): TicketRepository = JdbcTemplateTicketRepository(jdbcTemplate)

    @Bean
    fun getTicketUseCase(ticketRepository: TicketRepository): GetATicketUseCase = GetATicketUseCaseImpl(ticketRepository)

    @Bean
    fun createTicketUseCase(ticketRepository: TicketRepository): CreateTicketUseCase = CreateTicketUseCaseImpl(ticketRepository)

    @Bean
    fun updateTicketUseCase(ticketRepository: TicketRepository): UpdateTicketUseCase = UpdateTicketUseCaseImpl(ticketRepository)

}
