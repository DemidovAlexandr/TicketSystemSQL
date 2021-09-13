package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.dto.out.TicketOutDTO;
import com.demidov.ticketsystemsql.entities.Event;
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

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getAllByEventId(Integer id) {
        List<Ticket> ticketList = ticketRepository.findAllByEvent_IdOrderByLineNumberAscSeatNumberAsc(id);
        if (!ticketList.isEmpty()) {
            return ticketList;
        } else throw new CommonAppException("No tickets found for event with id: " + id);
    }

    @Transactional
    public Ticket create(TicketInDTO dto) {
        if (checkIfNotUnique(dto)) {
            throw new CommonAppException(NOT_UNIQUE_TICKET + dto);
        } else {
            Ticket ticket = new Ticket();
            setData(ticket, dto);
            ticket.getEvent().addTicket(ticket);
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Ticket update(TicketInDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_TICKET_MESSAGE + dto.getId()));

        if (checkIfNotUnique(dto)) {
            throw new CommonAppException(NOT_UNIQUE_TICKET + dto);
        } else {
            setData(ticket, dto);
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            Event event = ticket.getEvent();
            event.removeTicket(ticket);
            ticketRepository.deleteById(id);
        } else throw new CommonAppException(NO_TICKET_MESSAGE + id);
    }

    private boolean checkIfNotUnique(TicketInDTO dto) {
        Integer rowNumber = dto.getLineNumber();
        Integer seatNumber = dto.getSeatNumber();
        Integer eventId = dto.getEventId();
        return ticketRepository.findByEvent_IdAndLineNumberAndSeatNumber(eventId, rowNumber, seatNumber).isPresent();
    }

    public TicketInDTO toInDTO(Ticket ticket) {
        if (ticket == null) throw new CommonAppException(DTO_IS_NULL);
        TicketInDTO dto = new TicketInDTO();
        dto.setEventId(ticket.getEvent().getId());
        dto.setPurchaseId(ticket.getPurchase() != null ? ticket.getPurchase().getId() : null);
        dto.setPrice(ticket.getPrice());
        dto.setLineNumber(ticket.getLineNumber());
        dto.setSeatNumber(ticket.getSeatNumber());

        return dto;
    }

    public TicketOutDTO toOutDTO(Ticket ticket) {
        if (ticket == null) throw new CommonAppException(DTO_IS_NULL);
        TicketOutDTO dto = new TicketOutDTO();
        dto.setId(ticket.getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setPurchaseId(ticket.getPurchase() != null ? ticket.getPurchase().getId() : null);
        dto.setPrice(ticket.getPrice());
        dto.setLineNumber(ticket.getLineNumber());
        dto.setSeatNumber(ticket.getSeatNumber());

        return dto;
    }

    private void setData(Ticket ticket, TicketInDTO dto) {

        ticket.setLineNumber(dto.getLineNumber());
        ticket.setSeatNumber(dto.getSeatNumber());
        ticket.setPrice(dto.getPrice());

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new CommonAppException(NO_EVENT_MESSAGE + dto.getEventId()));
        ticket.setEvent(event);

    }
}
