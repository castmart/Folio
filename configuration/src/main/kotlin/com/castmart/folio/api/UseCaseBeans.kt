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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class UseCaseBeans {

    @Bean
    fun springJdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun jdbcTicketRepository(jdbcTemplate: JdbcTemplate): TicketRepository = JdbcTemplateTicketRepository(jdbcTemplate)

    @Bean
    fun getTicketUseCase(ticketRepository: TicketRepository): GetATicketUseCase = GetATicketUseCaseImpl(ticketRepository)

    @Bean
    fun createTicketUseCase(ticketRepository: TicketRepository): CreateTicketUseCase = CreateTicketUseCaseImpl(ticketRepository)

    @Bean
    fun updateTicketUseCase(ticketRepository: TicketRepository): UpdateTicketUseCase = UpdateTicketUseCaseImpl(ticketRepository)

    @Bean
    fun entrypoint(getUseCase: GetATicketUseCase, createTicketUseCase: CreateTicketUseCase, updateTicketUseCase: UpdateTicketUseCase): TicketRestEntrypointV1 {
        return TicketRestEntrypointV1(createTicketUseCase, updateTicketUseCase, getUseCase)
    }
}
