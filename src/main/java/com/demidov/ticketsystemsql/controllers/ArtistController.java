package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.ArtistInDTO;
import com.demidov.ticketsystemsql.dto.out.ArtistOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.ArtistWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/artists")
public class ArtistController {

    private final ArtistWebService webService;

    @Autowired
    public ArtistController(ArtistWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    ArtistOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<ArtistOutDTO> getAll() {
        return webService.getAll();
    }

    @PostMapping
    public ArtistOutDTO create(@RequestBody ArtistInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public ArtistOutDTO update(@RequestBody ArtistInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }

}
