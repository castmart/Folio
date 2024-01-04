package com.castmart.folio.details.entrypoint.function

import com.castmart.core.entity.TicketStatus
import com.castmart.core.usecase.CreateTicketUseCase
import com.castmart.core.usecase.GetATicketUseCase
import com.castmart.core.usecase.UpdateTicketUseCase
import com.castmart.folio.details.entrypoint.TicketDTOV1
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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

    private val entrypoint = TicketRestFunctionEntrypointV1(createUseCase, updateTicketUseCase, getUseCase)

    private val request = mockk<ServerRequest>()

    init {
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

                assertThrows<NoSuchElementException> {
                    entrypoint.getTicketById(request)
                }
            }
        }
    }
}
