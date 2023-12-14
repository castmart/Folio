package com.castmart.core.port

import com.castmart.core.entity.Ticket
import java.util.UUID

interface TicketRepository {

    fun get(id: UUID): Ticket

    fun save(ticket: Ticket): Ticket

    fun update(ticket: Ticket): Ticket
}