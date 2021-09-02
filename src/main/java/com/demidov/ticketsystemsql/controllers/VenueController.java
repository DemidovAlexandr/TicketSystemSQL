package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.dto.out.VenueOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.VenueWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/venues")
public class VenueController {

    private final VenueWebService webService;

    @Autowired
    public VenueController(VenueWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    @ResponseBody public VenueOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/all")
    @ResponseBody public List<VenueOutDTO> getAll() {
        return webService.getAll();
    }

    @PostMapping
    @ResponseBody public VenueOutDTO create(@RequestBody VenueInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    @ResponseBody public VenueOutDTO update(@RequestBody VenueInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody public String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }
}
