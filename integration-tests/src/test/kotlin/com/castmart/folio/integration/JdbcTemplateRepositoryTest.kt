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

class JdbcTemplateRepositoryTest : DescribeSpec() {
    init {

        // Configure the Database container
        val dbContainer =
            PostgreSQLContainer<Nothing>("postgres:15.5-alpine").apply {
                withDatabaseName("folio")
                withInitScript("schema.sql")
                startupAttempts = 1
                withUrlParam("stringtype", "unspecified")
            }
        val dataSource =
            install(JdbcDatabaseContainerExtension(dbContainer)) {
                minimumIdle = 1
                maximumPoolSize = 2
            }

        val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
        val jdbcRepository = JdbcTemplateTicketRepository(jdbcTemplate)
        val existingTicketId = UUID.randomUUID()
        val existingTicket =
            Ticket(
                id = existingTicketId,
                ticketNumber = "001",
                ownerName = "John",
                ownerEmail = "terminator.target@gmail.com",
                ownerPhoneNumber = "29081999",
                shoeDescription = "a shoe",
                completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                status = TicketStatus.IN_PROGRESS,
            )

        describe("Interacting with DB for ticket repository") {
            beforeEach {
                // Reset table
                jdbcTemplate.update("delete from ticket", MapSqlParameterSource())
                jdbcRepository.save(
                    existingTicket,
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
                val newTicket =
                    Ticket(
                        id = UUID.randomUUID(),
                        ticketNumber = "001",
                        ownerName = "John",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "29081999",
                        shoeDescription = "a shoe",
                        completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                        status = TicketStatus.IN_PROGRESS,
                    )
                val ticketCreated = jdbcRepository.save(newTicket)

                ticketCreated.id shouldBe newTicket.id
                ticketCreated.ticketNumber shouldBe newTicket.ticketNumber
                ticketCreated.ownerName shouldBe newTicket.ownerName
                ticketCreated.ownerEmail shouldBe newTicket.ownerEmail
                ticketCreated.ownerPhoneNumber shouldBe newTicket.ownerPhoneNumber
//                ticketCreated.completionDate shouldBe existingTicket.completionDate
                ticketCreated.shoeDescription shouldBe newTicket.shoeDescription
                ticketCreated.status shouldBe newTicket.status
            }

            it("Updates a ticket") {
                val editedTicket =
                    Ticket(
                        id = existingTicketId,
                        ticketNumber = "XXX2",
                        ownerName = "Charles",
                        ownerPhoneNumber = "018000",
                        ownerEmail = "new-email@email.com",
                        shoeDescription = "New shoes, not the ones before",
                        completionDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(20),
                        status = TicketStatus.READY_FOR_PICKUP,
                    )

                val ticketUpdated = jdbcRepository.update(editedTicket)

                ticketUpdated.id shouldBe existingTicket.id
                ticketUpdated.ticketNumber shouldNotBe existingTicket.ticketNumber
                ticketUpdated.ownerName shouldNotBe existingTicket.ownerName
                ticketUpdated.ownerEmail shouldNotBe existingTicket.ownerEmail
                ticketUpdated.ownerPhoneNumber shouldNotBe existingTicket.ownerPhoneNumber
                ticketUpdated.shoeDescription shouldNotBe existingTicket.shoeDescription
                ticketUpdated.completionDate shouldNotBe existingTicket.completionDate
                ticketUpdated.status shouldNotBe existingTicket.status
            }
        }
    }
}
