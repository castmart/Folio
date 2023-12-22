package com.castmart.core

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCaseImpl
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.UUID

class UpdateTicketUseCaseTest : DescribeSpec({
    val ticketRepoMock = mockk<TicketRepository>()
    val useCase = UpdateTicketUseCaseImpl(ticketRepoMock)
    val ticketId = UUID.randomUUID()

    beforeTest {
        clearMocks(ticketRepoMock)

        every {
            ticketRepoMock.get(any())
        } returns
            Ticket(
                id = ticketId,
                ticketNumber = "00001",
                shoeDescription = "description",
                ownerName = "name",
                ownerEmail = "email_original",
                ownerPhoneNumber = "12345678",
                completionDate = OffsetDateTime.now().minusDays(3),
                status = TicketStatus.IN_PROGRESS,
            )
    }

    describe("Ticket Update use case") {
        it("Saves correctly the ticket and returns response") {
            val request =
                UpdateTicketUseCase.Request(
                    id = UUID.randomUUID(),
                    ticketNumber = "1",
                    shoeDescription = "description",
                    ownerName = "name",
                    ownerEmail = "email@",
                    ownerPhoneNumber = "",
                    approxCompletionDate = OffsetDateTime.now().plusDays(3),
                    updateStatus = "READY_FOR_PICKUP",
                )

            val ticketResponse =
                Ticket(
                    id = request.id,
                    ticketNumber = request.ticketNumber,
                    shoeDescription = request.shoeDescription,
                    ownerName = request.ownerName,
                    ownerEmail = request.ownerEmail,
                    ownerPhoneNumber = request.ownerPhoneNumber,
                    completionDate = request.approxCompletionDate,
                    status = TicketStatus.READY_FOR_PICKUP,
                )

            every {
                ticketRepoMock.update(any())
            } returns ticketResponse

            val updatedTicket = useCase.updateTicket(request)

            updatedTicket.id shouldBeEqual request.id
        }

        it("Validates Completion date update") {
            val ticketId = UUID.randomUUID()
            every {
                ticketRepoMock.get(any())
            } returns
                Ticket(
                    id = ticketId,
                    ticketNumber = "00001",
                    shoeDescription = "description",
                    ownerName = "name",
                    ownerEmail = "email_original",
                    ownerPhoneNumber = "12345678",
                    completionDate = OffsetDateTime.now().minusDays(3),
                    status = TicketStatus.IN_PROGRESS,
                )

            // Try to update cpmpletion date to be older than original
            val request =
                UpdateTicketUseCase.Request(
                    id = ticketId,
                    ticketNumber = "1",
                    shoeDescription = "description",
                    ownerName = "name",
                    ownerEmail = "email@",
                    ownerPhoneNumber = "",
                    approxCompletionDate = OffsetDateTime.now().plusDays(2),
                    updateStatus = "READY_FOR_PICKUP",
                )

            val ticketResponse =
                Ticket(
                    id = request.id,
                    ticketNumber = request.ticketNumber,
                    shoeDescription = request.shoeDescription,
                    ownerName = request.ownerName,
                    ownerEmail = request.ownerEmail,
                    ownerPhoneNumber = request.ownerPhoneNumber,
                    completionDate = request.approxCompletionDate,
                    status = TicketStatus.READY_FOR_PICKUP,
                )

            every {
                ticketRepoMock.update(any())
            } returns ticketResponse

            val updatedTicket = useCase.updateTicket(request)

            updatedTicket.id shouldBeEqual request.id
        }

        it("Throws Illegal Argument Exception on invalid Status") {
            assertThrows<IllegalArgumentException> {
                useCase.updateTicket(
                    UpdateTicketUseCase.Request(
                        id = UUID.randomUUID(),
                        ticketNumber = "1",
                        shoeDescription = "description",
                        ownerName = "name",
                        ownerEmail = "email@",
                        ownerPhoneNumber = "",
                        approxCompletionDate = OffsetDateTime.now(),
                        updateStatus = "INVALID_STATUS",
                    ),
                )
            }
            verify(exactly = 0) {
                ticketRepoMock.update(any())
            }
        }
    }
})
