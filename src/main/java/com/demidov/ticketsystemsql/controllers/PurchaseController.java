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
    public @ResponseBody
    PurchaseOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping(path = {"/all"})
    public @ResponseBody
    List<PurchaseOutDTO> getAll() {
        return webService.getAll();
    }

    @GetMapping(path = {"/search"})
    public @ResponseBody
    List<PurchaseOutDTO> getAllByUser(@RequestParam(value = "userId") Integer userId) {
        return webService.getAllByUser(userId);
    }

    @PostMapping
    public PurchaseOutDTO create(@RequestBody PurchaseInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping
    public PurchaseOutDTO update(@RequestBody PurchaseInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = {"/{id}"})
    public @ResponseBody
    String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }

}
