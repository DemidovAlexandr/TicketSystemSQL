package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.EventWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class MyEventController {

    private final EventWebService webService;

    @Autowired
    public MyEventController(EventWebService webService) {
        this.webService = webService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    EventOutDTO getById(@PathVariable Integer id,
                        @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getById(id, isDeleted);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    List<EventOutDTO> getAll(@RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAll(isDeleted);
    }

    @GetMapping("/venues/{id}")
    public @ResponseBody
    List<EventOutDTO> getAllByVenue(
            @PathVariable Integer id,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAllByVenue(id, isDeleted);
    }

    @GetMapping("/artists/{id}")
    public @ResponseBody
    List<EventOutDTO> getAllByArtist(
            @PathVariable Integer id,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAllByArtist(id, isDeleted);
    }

    @GetMapping("/search")
    public @ResponseBody
    List<EventOutDTO> searchAll(
            @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "genreId", required = false) Integer genreId,
            @RequestParam(value = "subgenreId", required = false) Integer subgenreId,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {

        return webService.search(fromDate, toDate, city, genreId, subgenreId, isDeleted);
    }

    @PostMapping()
    public EventOutDTO create(@RequestBody EventInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping()
    public EventOutDTO update(@RequestBody EventInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String deleteById(@PathVariable Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }

}
