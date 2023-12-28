package com.castmart.folio.details.repository

import com.castmart.core.entity.Ticket
import com.castmart.core.entity.TicketStatus
import com.castmart.core.port.TicketRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.lang.Exception
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

class JdbcTemplateTicketRepository(private val databaseTicketRepository: NamedParameterJdbcTemplate) : TicketRepository {
    override fun get(id: UUID): Ticket {
        val parameters = MapSqlParameterSource()
        parameters.addValue("id", id)
        val ticketResult: List<Ticket> =
            databaseTicketRepository.query(
                "SELECT id, ticket_number, owner_name, owner_email, owner_phone_number, owner_email, status, " +
                    "shoe_description, completion_date from public.ticket where id=:id",
                parameters,
            ) { it: ResultSet, _: Int ->
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
            }

        return ticketResult.first()
    }

    override fun save(ticket: Ticket): Ticket {
        val parameters = MapSqlParameterSource()
        parameters.addValue("id", ticket.id)
        parameters.addValue("ticketNumber", ticket.ticketNumber)
        parameters.addValue("name", ticket.ownerName)
        parameters.addValue("email", ticket.ownerEmail)
        parameters.addValue("phoneNumber", ticket.ownerPhoneNumber)
        parameters.addValue("status", ticket.status.name)
        parameters.addValue("description", ticket.shoeDescription)
        parameters.addValue("date", ticket.completionDate)
        val success =
            databaseTicketRepository.update(
                "insert into ticket(id, ticket_number, owner_name, owner_email, owner_phone_number, status, " +
                    "shoe_description, completion_date) " +
                    "values (:id,:ticketNumber,:name,:email,:phoneNumber,:status,:description,:date)",
                parameters,
            )
        if (success == 0) {
            throw Exception("Could not create ticket in the database")
        }

        return ticket
    }

    override fun update(ticket: Ticket): Ticket {
        val parameters = MapSqlParameterSource()
        parameters.addValue("id", ticket.id)
        parameters.addValue("ticketNumber", ticket.ticketNumber)
        parameters.addValue("name", ticket.ownerName)
        parameters.addValue("email", ticket.ownerEmail)
        parameters.addValue("phoneNumber", ticket.ownerPhoneNumber)
        parameters.addValue("status", ticket.status.name)
        parameters.addValue("description", ticket.shoeDescription)
        parameters.addValue("date", ticket.completionDate)

        val success =
            databaseTicketRepository.update(
                """
                    |update ticket set ticket_number=:ticketNumber, owner_email=:email, owner_name=:name,
                    |owner_phone_number=:phoneNumber, status=:status, shoe_description=:description, 
                    |completion_date=:date where id=:id
                """.trimMargin(),
                parameters,
            )
        if (success == 0) {
            throw Exception("Could not update ticket in the database")
        }

        return ticket
    }
}
