package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Optional<List<Ticket>> findAllByEvent_IdOrderByRowNumberAscSeatNumberAsc(Integer eventId);

    Optional<Ticket> findByEvent_IdAndPriceAndRowNumberAndSeatNumber(Integer eventId, Integer price, Integer rowNumber, Integer SeatNumber);
}
