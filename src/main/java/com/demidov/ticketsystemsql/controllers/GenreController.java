package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.GenreInDTO;
import com.demidov.ticketsystemsql.dto.out.GenreOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.GenreWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/genres")
public class GenreController {
    private final GenreWebService webService;

    @Autowired
    public GenreController(GenreWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    GenreOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/search")
    public @ResponseBody
    GenreOutDTO getByName(@RequestParam(value = "name") String name) {
        return webService.getByName(name);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<GenreOutDTO> getAll() {
        return webService.getAll();
    }

    @PostMapping
    public GenreOutDTO create(@RequestBody GenreInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public GenreOutDTO update(@RequestBody GenreInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.getValue() + id;
    }
}
