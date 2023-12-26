package com.castmart.folio.api

import com.castmart.folio.details.entrypoint.TicketRestEntrypointV1
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@WebMvcTest(controllers = [TicketRestEntrypointV1::class])
@ContextConfiguration(classes = [TicketHttpHandlerMocks::class, TicketRestEntrypointV1::class])
class TicketEntrypointV1Test(
    @Autowired val mockMvc: MockMvc,
) : DescribeSpec() {
    init {
        extensions(SpringExtension)
        val v1Path = "/ticket/v1"

        describe("Get a ticket by id") {

            it("Returns a ticket DTO v1 when the ticket is found") {
                println("initializing test")
                mockMvc.get("$v1Path/${UUID.randomUUID()}") {
                    header("Accept", "*/*")
                }
                    .andExpect {
                        status { isOk() }
                        //                    content { jsonPath("$.id", assertNotNull()) }
                    }
            }
            it("Returns 404 when ticket not found") {
            }
        }
    }
}
