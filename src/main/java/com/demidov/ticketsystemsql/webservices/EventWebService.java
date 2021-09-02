package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventWebService {

    private final EventService eventService;

    public EventOutDTO getById(Integer id) {
        return eventService.toOutDTO(eventService.getById(id));
    }

    public List<EventOutDTO> getAll() {
        return eventService.getAll().stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<EventOutDTO> getAllByArtist(Integer artistId) {
        return eventService.getAllByArtist(artistId).stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<EventOutDTO> getAllByDateGenreCity(LocalDateTime date, Integer genreId, String city) {
        return eventService.getAllByDateGenreCity(date, genreId, city)
                .stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventOutDTO create(EventInDTO dto){
        return eventService.toOutDTO(eventService.create(dto));
    }

    @Transactional
    public EventOutDTO update(EventInDTO dto) {
        return eventService.toOutDTO(eventService.update(dto));
    }

    @Transactional
    public void deleteById(Integer id) {
        eventService.deleteById(id);
    }
}
