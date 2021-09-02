package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.dto.out.TicketOutDTO;
import com.demidov.ticketsystemsql.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketWebService {

    private final TicketService ticketService;

    public TicketOutDTO getById(Integer id){
        return ticketService.toOutDTO(ticketService.getById(id));
    }

    public List<TicketOutDTO> getAllByEvent(Integer eventId) {
        return ticketService.getAllByEventId(eventId)
                .stream()
                .map(ticketService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketOutDTO create(TicketInDTO dto) {
        return ticketService.toOutDTO(ticketService.create(dto));
    }

    @Transactional
    public TicketOutDTO update(TicketInDTO dto) {
        return ticketService.toOutDTO(ticketService.update(dto));
    }

    @Transactional
    public void deleteById(Integer id) {
        ticketService.deleteById(id);
    }
}
