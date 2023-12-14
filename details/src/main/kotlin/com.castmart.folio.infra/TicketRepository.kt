package com.castmart.folio.infra

import com.castmart.core.entity.Ticket
import com.castmart.core.port.TicketRepository
import org.springframework.jdbc.core.JdbcTemplate
import java.lang.Exception
import java.util.*


class TicketRepository(val databaseTicketRepository: JdbcTemplate): TicketRepository {

    override fun get(id: UUID): Ticket {
        TODO("Not yet implemented")
    }

    override fun save(ticket: Ticket): Ticket {
        val success = databaseTicketRepository.update(
            "insert into ticket values (?,?,?,?,?,?,?,?)",
            ticket.id,
            ticket.ticketNumber,
            ticket.ownerEmail,
            ticket.ownerName,
            ticket.ownerPhoneNumber,
            ticket.status.name,
            ticket.shoeDescription,
            ticket.completionDate
        )
        if (success == 0) {
            throw Exception("Could not create ticket in the database")
        }

        return ticket
    }

    override fun update(ticket: Ticket): Ticket {
        TODO("Not yet implemented")
    }
}