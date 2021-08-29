package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
