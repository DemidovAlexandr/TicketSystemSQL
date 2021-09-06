package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.dto.out.TicketOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.TicketWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/tickets"})
public class TicketController {

    private final TicketWebService webService;

    @Autowired
    public TicketController(TicketWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = {"/{id}"})
    @ResponseBody public TicketOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = {"/{eventId}"})
    @ResponseBody public List<TicketOutDTO> getByEventId(@PathVariable Integer eventId) {
        return webService.getAllByEvent(eventId);
    }

    @PostMapping
    @ResponseBody public TicketOutDTO create(@RequestBody TicketInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    @ResponseBody public TicketOutDTO update(@RequestBody TicketInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = {"/{id}"})
    @ResponseBody public String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }
}
