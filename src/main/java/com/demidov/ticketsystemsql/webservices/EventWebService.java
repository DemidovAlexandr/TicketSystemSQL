package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventWebService {

    private final EventService eventService;

    public EventOutDTO getById(Integer id, boolean isDeleted) {
        return eventService.toOutDTO(eventService.getById(id, isDeleted));
    }

    public List<EventOutDTO> getAll(boolean isDeleted) {
        return eventService.getAll(isDeleted).stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<EventOutDTO> getAllByArtist(Integer artistId, boolean isDeleted) {
        return eventService.getAllByArtist(artistId, isDeleted).stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<EventOutDTO> getAllByDate(LocalDate date, boolean isDeleted) {
        return eventService.getAllByDate(date, isDeleted)
                .stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<EventOutDTO> getAllByDateGenreCity(LocalDate date, Integer genreId, String city, boolean isDeleted) {
        return eventService.getAllByDateGenreCity(date, genreId, city, isDeleted)
                .stream()
                .map(eventService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventOutDTO create(EventInDTO dto) {
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
