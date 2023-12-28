package com.castmart.folio.api

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

@WebMvcTest(controllers = [TicketRestEntrypointV1::class])
@ContextConfiguration(classes = [TestContainerConfBean::class, UseCaseBeans::class, TicketRestEntrypointV1::class])
class TicketEntrypointV1Test(
    @Autowired val mockMvc: MockMvc,
    @Autowired val jdbcContainerExtension: JdbcDatabaseContainerExtension,
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val jdbcTicketRepository: TicketRepository
) : DescribeSpec() {
    init {
        extensions(SpringExtension)
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
                }
                    .andExpect {
                        status { isOk() }
                        //                    content { jsonPath("$.id", assertNotNull()) }
                    }
            }
            it("Returns 404 when ticket not found") {
                mockMvc.get("$v1Path/${UUID.randomUUID()}") {
                    header("Accept", "*/*")
                }
                    .andExpect {
                        status { isNotFound() }
                        //                    content { jsonPath("$.id", assertNotNull()) }
                    }
            }
        }
    }
}
