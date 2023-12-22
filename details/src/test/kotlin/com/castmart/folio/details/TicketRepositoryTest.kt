package com.castmart.folio.details

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.folio.details.repository.JdbcTemplateTicketRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.NoSuchElementException

class TicketRepositoryTest : DescribeSpec({
    val jdbcTemplate = mockk<JdbcTemplate>()
    val repository = JdbcTemplateTicketRepository(jdbcTemplate)

    describe("JdbcTicketRepository") {
        it("Throws exception when saving ticket in the database fails") {
            every { jdbcTemplate.update(any<String>()) } returns 0

            val ticket =
                Ticket(
                    id = UUID.randomUUID(),
                    ticketNumber = "1",
                    shoeDescription = "",
                    ownerName = "",
                    ownerPhoneNumber = "",
                    ownerEmail = "",
                    completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                    status = TicketStatus.IN_PROGRESS,
                )

            assertThrows<Exception> {
                repository.save(
                    ticket,
                )
            }
        }

        it("Saves the new ticket and returns number of rows affected") {
            val ticket =
                Ticket(
                    id = UUID.randomUUID(),
                    ticketNumber = "0001",
                    ownerName = "Jhon Connor",
                    ownerPhoneNumber = "10100101",
                    ownerEmail = "terminator.target@gmail.com",
                    shoeDescription = "Hey, those are goos shoes!",
                    completionDate = OffsetDateTime.now().plusDays(7),
                    status = TicketStatus.IN_PROGRESS,
                )

            every { jdbcTemplate.update(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns 1

            val result = repository.save(ticket)

            result.id shouldBeEqual ticket.id
            result.ticketNumber shouldBeEqual ticket.ticketNumber
            result.ownerName shouldBeEqual ticket.ownerName
            result.ownerEmail shouldBeEqual ticket.ownerEmail
            result.ownerPhoneNumber shouldBeEqual ticket.ownerPhoneNumber
            result.shoeDescription shouldBeEqual ticket.shoeDescription
            result.completionDate shouldBeEqual ticket.completionDate
            result.status shouldBeEqual ticket.status
        }

        it("Updates ticket returns 1 when update is successful") {
            val ticket =
                Ticket(
                    id = UUID.randomUUID(),
                    ticketNumber = "0001",
                    ownerName = "Jhon Connor",
                    ownerPhoneNumber = "10100101",
                    ownerEmail = "terminator.target@gmail.com",
                    shoeDescription = "Hey, those are goos shoes!",
                    completionDate = OffsetDateTime.now().plusDays(7),
                    status = TicketStatus.IN_PROGRESS,
                )

            every { jdbcTemplate.update(any(), any(), any(), any(), any(), any(), any(), any()) } returns 1

            val result = repository.update(ticket)

            result.id shouldBeEqual ticket.id
            result.ticketNumber shouldBeEqual ticket.ticketNumber
            result.ownerName shouldBeEqual ticket.ownerName
            result.ownerEmail shouldBeEqual ticket.ownerEmail
            result.ownerPhoneNumber shouldBeEqual ticket.ownerPhoneNumber
            result.shoeDescription shouldBeEqual ticket.shoeDescription
            result.completionDate shouldBeEqual ticket.completionDate
            result.status shouldBeEqual ticket.status
        }

        it("Get a ticket by id throws Not such element exception for non-existing ticket") {
            every { jdbcTemplate.query(any(), any<RowMapper<Ticket>>(), any()) } returns emptyList<Ticket>()

            assertThrows<NoSuchElementException> {
                repository.get(UUID.randomUUID())
            }
        }

        it("Get a ticket by id returns a domain ticket") {
            val ticket =
                Ticket(
                    id = UUID.randomUUID(),
                    ticketNumber = "0001",
                    ownerName = "Jhon Connor",
                    ownerPhoneNumber = "10100101",
                    ownerEmail = "terminator.target@gmail.com",
                    shoeDescription = "Hey, those are goos shoes!",
                    completionDate = OffsetDateTime.now().plusDays(7),
                    status = TicketStatus.IN_PROGRESS,
                )

            every { jdbcTemplate.query(any(), any<RowMapper<Ticket>>(), any()) } returns listOf(ticket)

            val result = repository.get(ticket.id)

            result.id shouldBeEqual ticket.id
            result.ticketNumber shouldBeEqual ticket.ticketNumber
            result.ownerName shouldBeEqual ticket.ownerName
            result.ownerEmail shouldBeEqual ticket.ownerEmail
            result.ownerPhoneNumber shouldBeEqual ticket.ownerPhoneNumber
            result.shoeDescription shouldBeEqual ticket.shoeDescription
            result.completionDate shouldBeEqual ticket.completionDate
            result.status shouldBeEqual ticket.status
        }
    }
})
