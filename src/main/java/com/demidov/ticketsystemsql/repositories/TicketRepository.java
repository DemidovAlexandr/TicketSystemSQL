package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("select t from Ticket t where t.id in :idList")
    List<Ticket> findAllById(List<Integer> idList);

    @Query("select t from Ticket t where t.id in :idList and (t.purchase.id is null or t.purchase.id = :purchaseId)")
    List<Ticket> findAllByIdAvailable(List<Integer> idList, @Nullable Integer purchaseId);

    List<Ticket> findAllByEvent_IdOrderByLineNumberAscSeatNumberAsc(Integer eventId);

    Optional<Ticket> findByEvent_IdAndLineNumberAndSeatNumber(Integer eventId, Integer lineNumber, Integer SeatNumber);
}
