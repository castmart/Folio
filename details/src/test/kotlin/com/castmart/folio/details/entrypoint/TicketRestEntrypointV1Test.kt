package com.castmart.folio.details.entrypoint

import com.castmart.core.entity.TicketStatus
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatusCode
import java.time.OffsetDateTime
import java.util.UUID

class TicketRestEntrypointV1Test : DescribeSpec() {
    private val getUseCase = mockk<GetATicketUseCase>()
    private val createUseCase = mockk<CreateTicketUseCase>()
    private val updateTicketUseCase = mockk<UpdateTicketUseCase>()

    private val entrypoint =
        TicketRestEntrypointV1(
            getATicketUseCase = getUseCase,
            updateTicketUseCase = updateTicketUseCase,
            createTicketUseCase = createUseCase,
        )

    init {

        describe("Get a ticket by id") {

            it("Returns a ticket DTO v1 when the ticket is found") {

                val getResponse =
                    GetATicketUseCase.Response(
                        id = UUID.randomUUID(),
                        ticketNumber = "0001",
                        ownerName = "John Connor",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "1",
                        shoeDescription = "A shoe",
                        approxCompletionDate = OffsetDateTime.now().plusDays(3),
                        status = TicketStatus.IN_PROGRESS,
                    )

                every {
                    getUseCase.getTicket(getResponse.id)
                } returns getResponse

                val response = entrypoint.getTicketById(getResponse.id)

                response.statusCode shouldBe HttpStatusCode.valueOf(200)
                val body = response.body!!
                body.id shouldBe getResponse.id
                body.ticketNumber shouldBe getResponse.ticketNumber
                body.ownerName shouldBe getResponse.ownerName
                body.ownerEmail shouldBe getResponse.ownerEmail
                body.ownerPhoneNumber shouldBe getResponse.ownerPhoneNumber
                body.shoeDescription shouldBe getResponse.shoeDescription
                body.completionDate shouldBe getResponse.approxCompletionDate
                body.status shouldBe getResponse.status.name
            }
            it("Use case throws Exception when ticket not found") {

                every {
                    getUseCase.getTicket(any())
                } throws NoSuchElementException()

                assertThrows<NoSuchElementException> {
                    entrypoint.getTicketById(UUID.randomUUID())
                }
            }
        }

        describe("Create ticket entrypoint") {
            it("Creates a ticket returns the correct response and code ticket ") {
                val requestDTOV1 =
                    TicketDTOV1(
                        id = UUID.randomUUID(),
                        ticketNumber = "1",
                        ownerName = "John Connor",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "1",
                        shoeDescription = "A shoe",
                        completionDate = OffsetDateTime.now().plusDays(3),
                        status = "null",
                    )

                val responseObject =
                    CreateTicketUseCase.Response(
                        id = UUID.randomUUID(),
                        ticketNumber = "1",
                        ownerName = "John Connor",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "1",
                        shoeDescription = "A shoe",
                        approxCompletionDate = requestDTOV1.completionDate,
                        status = TicketStatus.IN_PROGRESS,
                    )

                every {
                    createUseCase.createTicket(any())
                } returns responseObject

                val response = entrypoint.createTicket(requestDTOV1)

                response.statusCode shouldBe HttpStatusCode.valueOf(200)
                val body = response.body!!
                body.id shouldNotBe null
                body.ticketNumber shouldBe requestDTOV1.ticketNumber
                body.ownerName shouldBe requestDTOV1.ownerName
                body.ownerEmail shouldBe requestDTOV1.ownerEmail
                body.ownerPhoneNumber shouldBe requestDTOV1.ownerPhoneNumber
                body.shoeDescription shouldBe requestDTOV1.shoeDescription
                body.completionDate shouldBe requestDTOV1.completionDate
                body.status shouldBe TicketStatus.IN_PROGRESS.name
            }
        }

        describe("Update ticket entrypoint") {
            it("Update a ticket returns the correct response ") {
                val requestDTOV1 =
                    TicketDTOV1(
                        id = UUID.randomUUID(),
                        ticketNumber = "1",
                        ownerName = "John Connor",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "1",
                        shoeDescription = "A shoe",
                        completionDate = OffsetDateTime.now().plusDays(3),
                        status = "null",
                    )

                val responseObject =
                    UpdateTicketUseCase.Response(
                        id = UUID.randomUUID(),
                        ticketNumber = "1",
                        ownerName = "John Connor",
                        ownerEmail = "terminator.target@gmail.com",
                        ownerPhoneNumber = "1",
                        shoeDescription = "A shoe",
                        approxCompletionDate = requestDTOV1.completionDate,
                        status = TicketStatus.IN_PROGRESS,
                    )

                every {
                    updateTicketUseCase.updateTicket(any())
                } returns responseObject

                val response = entrypoint.updateTicket(requestDTOV1)

                response.statusCode shouldBe HttpStatusCode.valueOf(200)
                val body = response.body!!
                body.id shouldNotBe null
                body.ticketNumber shouldBe requestDTOV1.ticketNumber
                body.ownerName shouldBe requestDTOV1.ownerName
                body.ownerEmail shouldBe requestDTOV1.ownerEmail
                body.ownerPhoneNumber shouldBe requestDTOV1.ownerPhoneNumber
                body.shoeDescription shouldBe requestDTOV1.shoeDescription
                body.completionDate shouldBe requestDTOV1.completionDate
                body.status shouldBe TicketStatus.IN_PROGRESS.name
            }
        }
    }
}
