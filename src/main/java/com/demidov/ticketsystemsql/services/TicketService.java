package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.dto.out.SubgenreOutDTO;
import com.demidov.ticketsystemsql.dto.out.TicketOutDTO;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.entities.Ticket;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.EventRepository;
import com.demidov.ticketsystemsql.repositories.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final static String NO_TICKET_MESSAGE = "There is no such ticket with id: ";
    private final static String NO_EVENT_MESSAGE = "There is no such event with id: ";
    private final static String NOT_UNIQUE_TICKET = "This ticket is not unique: ";
    private final static String DTO_IS_NULL = "DTO must not be null";

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper mapper;

    public Ticket getById(Integer id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get();
        } else throw new CommonAppException(NO_TICKET_MESSAGE + id);
    }

    public List<Ticket> getByEventId(Integer id) {
        Optional<List<Ticket>> optionalTickets = ticketRepository.findAllByEvent_IdOrderByRowNumberAscSeatNumberAsc(id);
        if (optionalTickets.isPresent()) {
            return optionalTickets.get();
        } else throw new CommonAppException("No tickets found for event with id: " + id);
    }

    @Transactional
    public Ticket create(Integer rowNumber, Integer seatNumber, Integer price, Integer eventId) {
        Ticket ticket = new Ticket();
        ticket.setRowNumber(rowNumber);
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(price);
        if (eventRepository.existsById(eventId)) {
            ticket.setEvent(eventRepository.getById(eventId));
        } else throw new CommonAppException(NO_EVENT_MESSAGE + eventId);
        if (!checkIfUnique(ticket)) {
            throw new CommonAppException(NOT_UNIQUE_TICKET + ticket);
        } else return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(Integer id, Integer rowNumber, Integer seatNumber, Integer price, Integer eventId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setRowNumber(rowNumber);
            ticket.setSeatNumber(seatNumber);
            ticket.setPrice(price);
            if (eventRepository.existsById(eventId)) {
                ticket.setEvent(eventRepository.getById(eventId));
            } else throw new CommonAppException(NO_EVENT_MESSAGE + eventId);
            if (!checkIfUnique(ticket)) {
                throw new CommonAppException(NOT_UNIQUE_TICKET + optionalTicket);
            } else return ticketRepository.save(ticket);
        } else throw new CommonAppException(NO_TICKET_MESSAGE + id);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            ticketRepository.deleteById(id);
        } else throw new CommonAppException(NO_TICKET_MESSAGE + id);
    }

    private boolean checkIfUnique(Ticket ticket) {
        Integer rowNumber = ticket.getRowNumber();
        Integer seatNumber = ticket.getSeatNumber();
        Integer price = ticket.getPrice();
        Integer eventId = ticket.getEvent().getId();
        return ticketRepository.findByEvent_IdAndPriceAndRowNumberAndSeatNumber(eventId, price, rowNumber, seatNumber).isEmpty();
    }

    public TicketInDTO toInDTO(Ticket ticket) {
        return Optional.ofNullable(ticket)
                .map(entity -> mapper.convertValue(entity, TicketInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public TicketOutDTO toOutDTO(Ticket ticket) {
        return Optional.ofNullable(ticket)
                .map(entity -> mapper.convertValue(entity, TicketOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }
}
