package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.dto.out.SubgenreOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.SubgenreWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/genres/subgenres")
public class SubgenreController {

    private final SubgenreWebService webService;

    @Autowired
    public SubgenreController(SubgenreWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    SubgenreOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<SubgenreOutDTO> getAll() {
        return webService.getAll();
    }

    @PostMapping
    public SubgenreOutDTO create(@RequestBody SubgenreInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public SubgenreOutDTO update(@RequestBody SubgenreInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }
}
