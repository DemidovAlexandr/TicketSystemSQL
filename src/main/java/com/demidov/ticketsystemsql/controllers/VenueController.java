package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.dto.out.VenueOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.VenueWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/venues")
public class VenueController {

    private final VenueWebService webService;

    @Autowired
    public VenueController(VenueWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    VenueOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<VenueOutDTO> getAll(@RequestParam(value = "city", required = false) String city) {
        if (city == null) {
            return webService.getAll();
        } else {
            return webService.getAllByCity(city);
        }
    }


    @PostMapping
    public VenueOutDTO create(@RequestBody VenueInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public VenueOutDTO update(@RequestBody VenueInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }
}
