package com.castmart.core

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.EntityNotFoundException
import com.castmart.core.port.TicketRepository
import com.castmart.core.usecase.GetATicketUseCaseImpl
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*

class GetATicketUseCaseTest: DescribeSpec({
    val ticketRepositoryMock = mockk<TicketRepository>()
    val useCase = GetATicketUseCaseImpl(ticketRepositoryMock)

    describe("Get a ticket use case") {
        it("The ticket is not found then ") {
            every { ticketRepositoryMock.get(any()) } throws EntityNotFoundException()
            assertThrows<EntityNotFoundException> {
                useCase.getTicket(UUID.randomUUID())
            }
        }

        it("The ticket is found") {
            val ticketId = UUID.randomUUID()
            val ticket = Ticket(
                id = ticketId,
                ticketNumber = "0001",
                ownerName = "John Connor",
                ownerPhoneNumber = "110010101",
                ownerEmail = "terminator_target@gmail.com",
                shoeDescription = "N-Jordan from 90's",
                completionDate = OffsetDateTime.now().plusDays(7),
                status = TicketStatus.IN_PROGRESS
            )
            every {
                ticketRepositoryMock.get(ticketId)
            } returns ticket

            val returnedTicket = useCase.getTicket(ticketId)

            returnedTicket.id shouldBeEqual ticketId
            returnedTicket.ticketNumber shouldBeEqual ticket.ticketNumber
            returnedTicket.ownerEmail shouldBeEqual ticket.ownerEmail
            returnedTicket.ownerName shouldBeEqual ticket.ownerName
            returnedTicket.ownerPhoneNumber shouldBeEqual ticket.ownerPhoneNumber
            returnedTicket.shoeDescription shouldBeEqual ticket.shoeDescription
            returnedTicket.approxCompletionDate shouldBeEqual ticket.completionDate
            returnedTicket.status shouldBeEqual ticket.status.name
        }
    }
})