package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.UserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserWebService webService;

    @Autowired
    public UserController(UserWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    @ResponseBody public UserOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = "/all")
    @ResponseBody public List<UserOutDTO> getAll() {
        return webService.getAll();
    }

    @PostMapping
    public UserOutDTO create(@RequestBody UserInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public UserOutDTO update(@RequestBody UserInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody public String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }
}
