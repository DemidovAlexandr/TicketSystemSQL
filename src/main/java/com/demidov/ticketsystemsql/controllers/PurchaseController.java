package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.PurchaseInDTO;
import com.demidov.ticketsystemsql.dto.out.PurchaseOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.PurchaseWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/purchases"})
public class PurchaseController {

    private final PurchaseWebService webService;

    @Autowired
    public PurchaseController(PurchaseWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = {"/{id}"})
    @ResponseBody public PurchaseOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = {"/all"})
    @ResponseBody public List<PurchaseOutDTO> getAll() {
        return webService.getAll();
    }

    @GetMapping(path = {"/{userId}"})
    @ResponseBody public List<PurchaseOutDTO> getAllByUser(@PathVariable Integer userId) {
        return webService.getAllByUser(userId);
    }

    @PostMapping
    @ResponseBody public PurchaseOutDTO create(@RequestBody PurchaseInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    @ResponseBody public PurchaseOutDTO update(@RequestBody PurchaseInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = {"/{id}"})
    @ResponseBody public String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }

}
