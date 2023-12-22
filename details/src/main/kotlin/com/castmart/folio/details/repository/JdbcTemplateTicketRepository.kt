package com.castmart.folio.details.repository

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import org.springframework.jdbc.core.JdbcTemplate
import java.lang.Exception
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

class JdbcTemplateTicketRepository(private val databaseTicketRepository: JdbcTemplate) : TicketRepository {
    override fun get(id: UUID): Ticket {
        val ticketResult: List<Ticket> =
            databaseTicketRepository.query(
                "SELECT id, ticket_number, owner_email, owner_phone_number, owner_email, status, " +
                    "shoe_description, completion_date where id=?",
                {
                        it: ResultSet, _: Int ->
                    Ticket(
                        id = UUID.fromString(it.getString("id")),
                        ticketNumber = it.getString("ticket_number"),
                        shoeDescription = it.getString("shoe_description"),
                        ownerEmail = it.getString("owner_email"),
                        ownerPhoneNumber = it.getString("owner_phone_number"),
                        ownerName = it.getString("owner_name"),
                        status = TicketStatus.valueOf(it.getString("status")),
                        completionDate =
                            OffsetDateTime.of(
                                it.getTimestamp("completion_date").toLocalDateTime(),
                                ZoneOffset.UTC,
                            ),
                    )
                },
                arrayOf(id.toString()),
            )

        return ticketResult.first()
    }

    override fun save(ticket: Ticket): Ticket {
        val success =
            databaseTicketRepository.update(
                "insert into ticket values (?,?,?,?,?,?,?,?)",
                ticket.id,
                ticket.ticketNumber,
                ticket.ownerEmail,
                ticket.ownerName,
                ticket.ownerPhoneNumber,
                ticket.status.name,
                ticket.shoeDescription,
                ticket.completionDate,
            )
        if (success == 0) {
            throw Exception("Could not create ticket in the database")
        }

        return ticket
    }

    override fun update(ticket: Ticket): Ticket {
        val success =
            databaseTicketRepository.update(
                """update ticket set ticket_number=?, owner_email=?, owner_name=?,
                |owner_phone_number=?, status=?, show_description=?, completion_date=?
                |
                """.trimMargin(),
                ticket.ticketNumber,
                ticket.ownerEmail,
                ticket.ownerName,
                ticket.ownerPhoneNumber,
                ticket.status.name,
                ticket.shoeDescription,
                ticket.completionDate,
            )
        if (success == 0) {
            throw Exception("Could not update ticket in the database")
        }

        return ticket
    }
}
