package com.castmart.folio.infra

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.springframework.jdbc.core.JdbcTemplate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class TicketRepositoryTest: DescribeSpec({
    val jdbcTemplate = mockk<JdbcTemplate>()
    val repository = TicketRepository(jdbcTemplate)

    describe("JdbcTicketRepository") {
        it("Throws exception when saving ticket in the database fails") {
            every { jdbcTemplate.update(any<String>()) } returns 0

            val ticket = Ticket(
                id = UUID.randomUUID(),
                ticketNumber = "1",
                shoeDescription = "",
                ownerName = "",
                ownerPhoneNumber = "",
                ownerEmail = "",
                completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                status = TicketStatus.IN_PROGRESS
            )

            assertThrows<Exception>{
                repository.save(
                    ticket
                )
            }
        }
    }

})