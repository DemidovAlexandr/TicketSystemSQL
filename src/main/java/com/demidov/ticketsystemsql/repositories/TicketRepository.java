package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("select t from Ticket t where t.id in :idList")
    Optional<List<Ticket>> findAllById(List<Integer> idList);

    Optional<List<Ticket>> findAllByEvent_IdOrderByLineNumberAscSeatNumberAsc(Integer eventId);

    Optional<Ticket> findByEvent_IdAndPriceAndLineNumberAndSeatNumber(Integer eventId, Integer price, Integer rowNumber, Integer SeatNumber);
}
