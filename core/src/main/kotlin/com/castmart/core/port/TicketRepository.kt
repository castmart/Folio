package com.castmart.core.port

import com.castmart.core.entity.Ticket

interface TicketRepository {

    fun save(ticket: Ticket): Ticket
}