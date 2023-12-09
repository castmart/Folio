package com.castmart.core

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.CreateTicketUseCaseImpl
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateTicketUserCaseTest: DescribeSpec({
    val ticketRepoMock = mockk<TicketRepository>()
    val useCase = CreateTicketUseCaseImpl(ticketRepoMock)

    describe("Ticket creation use case") {
        it("throws and exception when invalid entity props") {

            every { ticketRepoMock.save(any()) } throws Exception()
            val request = CreateTicketUseCase.Request(
                "0001",
                "New Shoe never seen before, a name larger than column definition will throw an error",
                "Fulanito",
                "de Tal",
                "email@correo.com",
                OffsetDateTime.now().plusDays(2)
            )

            assertThrows<Exception> {
                useCase.createTicket(request)
            }

        }

        it ("saves correctly the ticket and returns the corresponding id") {
            val date = OffsetDateTime.now().plusDays(2)
            val request = CreateTicketUseCase.Request(
                "0001",
                "New Shoe never seen before, a name larger than column definition will throw an error",
                "Fulanito",
                "de Tal",
                "email@correo.com",
                date
            )
            val dbID = UUID.randomUUID()
            every { ticketRepoMock.save(any()) } returns Ticket(
                dbID,
                "0001",
                "New Shoe never seen before",
                "Fulanito",
                "de Tal",
                "email@correo.com",
                date,
                TicketStatus.IN_PROGRESS
            )


            val response = useCase.createTicket(request)

            assertNotNull(response.id)
            assertEquals(request.ticketNumber, response.ticketNumber)
            assertEquals(request.approxCompletionDate, response.approxCompletionDate)
        }

//        it ("Correctly sets the ticket status")
    }

})