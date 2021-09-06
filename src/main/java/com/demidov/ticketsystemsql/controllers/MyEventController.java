package com.demidov.ticketsystemsql.controllers;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.enums.ControllerMessages;
import com.demidov.ticketsystemsql.webservices.EventWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @ResponseBody public EventOutDTO getById(@PathVariable Integer id) {
        return webService.getById(id);
    }

    @GetMapping("/byArtist")
    @ResponseBody public List<EventOutDTO> getAllByArtistName(
            @RequestParam(value = "artistId") Integer artistId) {
        return webService.getAllByArtist(artistId);
    }

    //todo: Change LocalDateTime param to LocalDate
    @GetMapping("/byDateGenreCity")
    @ResponseBody public List<EventOutDTO> getAllByDateGenreCity(
            @RequestParam(value = "localDate") LocalDateTime localDate,
            @RequestParam(value = "genreId") Integer genreId,
            @RequestParam(value = "city") String city) {
        return webService.getAllByDateGenreCity(localDate, genreId, city);
    }

    @PostMapping()
    @ResponseBody public EventOutDTO create(@RequestBody EventInDTO dto) {
        return webService.create(dto);
    }

    @PutMapping()
    @ResponseBody public EventOutDTO update(@RequestBody EventInDTO dto) {
        return webService.update(dto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody public String deleteById(@RequestParam Integer id) {
        webService.deleteById(id);
        return ControllerMessages.DELETED.name() + id;
    }

}
