package com.castmart.folio.api

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import com.fasterxml.jackson.databind.ObjectMapper

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.mockk.core.ValueClassSupport.boxedValue
import org.hamcrest.Matchers.isA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.test.assertNotNull

@WebMvcTest(controllers = [TicketRestEntrypointV1::class])
@ContextConfiguration(classes = [TestContainerConfBean::class, UseCaseBeans::class, TicketRestEntrypointV1::class])
class TicketEntrypointV1Test(
    @Autowired val mockMvc: MockMvc,
    @Autowired val jdbcContainerExtension: JdbcDatabaseContainerExtension,
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val jdbcTicketRepository: TicketRepository,
    @Autowired val objectMapper: ObjectMapper,
) : DescribeSpec() {
    init {
        extensions(SpringExtension)
        // One container for all this Test Spec
        extension(jdbcContainerExtension)  // Important!! to handle the test lifecycle
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
                        status = TicketStatus.IN_PROGRESS
                    )
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
                val ticket = Ticket(
                    id = UUID.randomUUID(),
                    ticketNumber = "0001",
                    ownerName = "John",
                    ownerEmail = "connor.terminator@email.com",
                    ownerPhoneNumber = "29081999",
                    shoeDescription = "A metal shoe",
                    completionDate = OffsetDateTime.now(ZoneOffset.UTC),
                    status = TicketStatus.IN_PROGRESS
                )
                mockMvc.put(v1Path, ) {
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
                mockMvc.put(v1Path, ) {
                    contentType = MediaType.APPLICATION_JSON
                    content = """
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

        
    }
}
