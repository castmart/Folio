package com.castmart.folio.details.entrypoint.function

import com.castmart.core.entity.TicketStatus
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.TicketDTOV1
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatusCode
import org.springframework.web.servlet.function.EntityResponse
import org.springframework.web.servlet.function.ServerRequest
import java.time.OffsetDateTime
import java.util.UUID

class TicketRestFunctionEntryPointV1Test : DescribeSpec() {
    private val getUseCase = mockk<GetATicketUseCase>()
    private val createUseCase = mockk<CreateTicketUseCase>()
    private val updateTicketUseCase = mockk<UpdateTicketUseCase>()

    private val entrypoint = TicketRestEntrypointHandlerV1(createUseCase, updateTicketUseCase, getUseCase)

    private val request = mockk<ServerRequest>()

    init {
        beforeTest {
            clearMocks(getUseCase)
            clearMocks(createUseCase)
            clearMocks(updateTicketUseCase)
            clearMocks(request)
        }

        describe("Get a Ticket by id (functional endpoint)") {
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

                every {
                    request.pathVariable("ticketId")
                } returns getResponse.id.toString()

                val response = entrypoint.getTicketById(request) as EntityResponse<TicketDTOV1>

                response.statusCode() shouldBe HttpStatusCode.valueOf(200)
                val body = response.entity()

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

                every {
                    request.pathVariable("ticketId")
                } returns UUID.randomUUID().toString()


                val response = entrypoint.getTicketById(request)
                response.statusCode() shouldBe HttpStatusCode.valueOf(404)
            }
        }

        describe("Create ticket entrypoint (functional endpoint)") {
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

                every {
                    request.body(TicketDTOV1::class.java)
                } returns requestDTOV1

                val response = entrypoint.createTicket(request) as EntityResponse<TicketDTOV1>

                response.statusCode() shouldBe HttpStatusCode.valueOf(200)
                val body = response.entity()
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

        describe("Update ticket entrypoint (functional endpoint)") {
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

                every {
                    request.body(TicketDTOV1::class.java)
                } returns requestDTOV1

                val response = entrypoint.updateTicket(request) as EntityResponse<TicketDTOV1>

                response.statusCode() shouldBe HttpStatusCode.valueOf(200)
                val body = response.entity()
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
