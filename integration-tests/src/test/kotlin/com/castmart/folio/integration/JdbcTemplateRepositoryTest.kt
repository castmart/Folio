package com.castmart.folio.integration

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.folio.details.repository.JdbcTemplateTicketRepository
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID


class JdbcTemplateRepositoryTest: DescribeSpec() {

    init {

        // Configure the Database container
        val dbContainer = PostgreSQLContainer<Nothing>("postgres:15.5-alpine").apply {
            withDatabaseName("folio")
            withInitScript("schema.sql")
            startupAttempts = 1
            withUrlParam("stringtype", "unspecified")
        }
        val dataSource = install(JdbcDatabaseContainerExtension(dbContainer)) {
            minimumIdle = 1
            maximumPoolSize = 2
        }

        val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
        val jdbcRepository = JdbcTemplateTicketRepository(jdbcTemplate)
        val existingTicketId = UUID.randomUUID()
        val existingTicket = Ticket(
            id = existingTicketId,
            ticketNumber = "001",
            ownerName = "John",
            ownerEmail = "terminator.target@gmail.com",
            ownerPhoneNumber = "29081999",
            shoeDescription = "a shoe",
            completionDate = OffsetDateTime.now(ZoneOffset.UTC),
            status = TicketStatus.IN_PROGRESS
        )

        describe("Interacting with DB for ticket repository") {
            beforeEach {
                // Reset table
                jdbcTemplate.update("delete from ticket", MapSqlParameterSource())
                jdbcRepository.save(
                    existingTicket
                )
            }
            it("Returns an existing ticket") {
                val ticket = jdbcRepository.get(existingTicketId)

                ticket shouldNotBe null
                ticket.id shouldBe existingTicket.id
                ticket.ticketNumber shouldBe existingTicket.ticketNumber
                ticket.ownerName shouldBe existingTicket.ownerName
                ticket.ownerEmail shouldBe existingTicket.ownerEmail
                ticket.ownerPhoneNumber shouldBe existingTicket.ownerPhoneNumber
//                ticket.completionDate shouldBe existingTicket.completionDate
                ticket.shoeDescription shouldBe existingTicket.shoeDescription
                ticket.status shouldBe existingTicket.status
            }

            it("Creates a new ticket") {

            }

            it("Updates a ticket") {

            }
        }

    }

}