package com.castmart.folio.api

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import com.castmart.folio.details.entrypoint.annotated.TicketRestEntrypointV1
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@WebMvcTest(controllers = [TicketRestEntrypointV1::class])
@ContextConfiguration(classes = [TestContainerConfBean::class, UseCaseBeans::class, TicketRestEntrypointV1::class])
class TicketEntrypointV1Test(
    @Autowired val mockMvc: MockMvc,
    @Autowired val jdbcContainerExtension: JdbcDatabaseContainerExtension,
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val jdbcTicketRepository: TicketRepository,
    @Autowired val objectMapper: ObjectMapper,
) : DescribeSpec({
        extensions(SpringExtension)
        // One container for all this Test Spec
        extension(jdbcContainerExtension) // Important!! to handle the test lifecycle
        val v1Path = "/ticket/v1"
        val ticketId = UUID.randomUUID()

        describe("Get a ticket by id") {

            beforeEach {
                jdbcTemplate.update("delete from ticket", MapSqlParameterSource())
                jdbcTicketRepository.save(
                    Ticket(
                        id = ticketId,
                        ticketNumber = "",
                        ownerName = "",
                        ownerEmail = "",
                        ownerPhoneNumber = "",
                        shoeDescription = "",
                        completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                        status = TicketStatus.IN_PROGRESS,
                    ),
                )
            }

            it("Returns a ticket DTO v1 when the ticket is found") {
                mockMvc.get("$v1Path/$ticketId") {
                    header("Accept", "*/*")
                }.andExpect {
                    status { isOk() }
                    // ... Check the content
                }
            }

            it("Returns 404 when ticket not found") {
                mockMvc.get("$v1Path/${UUID.randomUUID()}") {
                    header("Accept", "*/*")
                }.andExpect {
                    status { isNotFound() }
                    // ... check no content
                }
            }
        }

        describe("Create ticket") {
            beforeEach {
                jdbcTemplate.update("delete from ticket", MapSqlParameterSource())
            }

            it("Creates a ticket and returns 200 ok with the ticket in the response body") {
                val ticket =
                    Ticket(
                        id = UUID.randomUUID(),
                        ticketNumber = "0001",
                        ownerName = "John",
                        ownerEmail = "connor.terminator@email.com",
                        ownerPhoneNumber = "29081999",
                        shoeDescription = "A metal shoe",
                        completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                        status = TicketStatus.IN_PROGRESS,
                    )
                mockMvc.put(v1Path) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(ticket)
                }.andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                    }
                    jsonPath("$.id") { isNotEmpty() }
                    jsonPath("$.ticketNumber") { value(ticket.ticketNumber) }
                    jsonPath("$.ownerName") { value(ticket.ownerName) }
                    jsonPath("$.ownerEmail") { value(ticket.ownerEmail) }
                    jsonPath("$.ownerPhoneNumber") { value(ticket.ownerPhoneNumber) }
                    jsonPath("$.shoeDescription") { value(ticket.shoeDescription) }
                    jsonPath("$.status") { value(ticket.status.name) }
                    jsonPath("$.completionDate") { value(ticket.completionDate.toString()) }
                }
            }

            it("Creates a ticket with invalid status throws 400 bad request") {
                mockMvc.put(v1Path) {
                    contentType = MediaType.APPLICATION_JSON
                    content =
                        """
                        { 
                            "id": "${UUID.randomUUID()}",
                            "ticketNumber": "0001",
                            "ownerName": "Juan",
                            "ownerEmail": "email@email.com",
                            "ownerPhoneNumber": "01800123",
                            "showDescription": "shoe",
                            "completionDate": "2040-12-12T00:00:00.000Z",
                            "status": "INVALID_STATE"
                        }
                        """.trimIndent()
                }.andExpect {
                    status { isBadRequest() }
                }
            }
        }

        describe("Update ticket") {
            val existingTicket =
                Ticket(
                    id = ticketId,
                    ticketNumber = "0001",
                    ownerName = "John Connor",
                    ownerEmail = "connor.john@skynet.com",
                    ownerPhoneNumber = "01800-123-456",
                    shoeDescription = "Show Description",
                    completionDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1),
                    status = TicketStatus.IN_PROGRESS,
                )

            beforeEach {
                jdbcTemplate.update("delete from ticket", MapSqlParameterSource())
                jdbcTicketRepository.save(
                    existingTicket,
                )
            }

            it("Updates correctly an existing ticket") {
                val updateRequest =
                    Ticket(
                        id = existingTicket.id,
                        ticketNumber = "T-8000",
                        ownerName = "Terminator",
                        ownerEmail = "t1000.arnold@skynet.com",
                        ownerPhoneNumber = "011000001",
                        shoeDescription = "No shoes required",
                        completionDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(3),
                        status = TicketStatus.READY_FOR_PICKUP,
                    )
                mockMvc.post(v1Path) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateRequest)
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.id") { value(updateRequest.id.toString()) }
                    jsonPath("$.ticketNumber") { value(updateRequest.ticketNumber) }
                    jsonPath("$.ownerName") { value(updateRequest.ownerName) }
                    jsonPath("$.ownerEmail") { value(updateRequest.ownerEmail) }
                    jsonPath("$.ownerPhoneNumber") { value(updateRequest.ownerPhoneNumber) }
                    jsonPath("$.shoeDescription") { value(updateRequest.shoeDescription) }
                    jsonPath("$.status") { value(updateRequest.status.name) }
                    jsonPath("$.completionDate") { value(updateRequest.completionDate.toString()) }
                }
            }

            it("Returns 400 on invalid state") {
                mockMvc.post(v1Path) {
                    contentType = MediaType.APPLICATION_JSON
                    content =
                        """
                        { 
                            "id": "${existingTicket.id}",
                            "ticketNumber": "0001",
                            "ownerName": "Juan",
                            "ownerEmail": "email@email.com",
                            "ownerPhoneNumber": "01800123",
                            "showDescription": "shoe",
                            "completionDate": "2040-12-12T00:00:00.000Z",
                            "status": "INVALID_STATE"
                        }
                        """.trimIndent()
                }.andExpect {
                    status { isBadRequest() }
                }
            }
        }
    })
