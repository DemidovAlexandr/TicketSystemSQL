package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.EventWebService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/artist/{id}")
    public @ResponseBody
    List<EventOutDTO> searchAll(
            @PathVariable Integer id,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAllByArtist(id, isDeleted);
    }

    @GetMapping("/byDate")
    public @ResponseBody
    List<EventOutDTO> getAllByDate(
            @RequestParam(value = "localDate") LocalDate localDate,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAllByDate(localDate, isDeleted);
    }

    @GetMapping("/byDateGenreCity")
    public @ResponseBody
    List<EventOutDTO> getAllByDateGenreCity(
            @RequestParam(value = "localDate") LocalDate localDate,
            @RequestParam(value = "genreId") Integer genreId,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return webService.getAllByDateGenreCity(localDate, genreId, city, isDeleted);
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
