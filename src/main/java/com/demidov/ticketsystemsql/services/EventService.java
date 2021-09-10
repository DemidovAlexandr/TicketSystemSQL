package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final static String NO_EVENT_MESSAGE = "There is no such event with id: ";
    private static final String DTO_IS_NULL = "DTO must not be null";

    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final SubgenreRepository subgenreRepository;
    private final VenueRepository venueRepository;
    private final TicketRepository ticketRepository;
    private final ObjectMapper mapper;

    public Event getById(Integer id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else throw new CommonAppException(NO_EVENT_MESSAGE + id);
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public List<Event> getAllByArtist(Integer artistId) {
        Optional<Artist> optionalArtist = artistRepository.findById(artistId);
        List<Event> events = List.of();
        if (optionalArtist.isPresent()) {
            events = eventRepository.findAllByArtistListContainingOrderByBeginDate(optionalArtist.get());
            if (events.isEmpty()) {
                log.info("No events found with this artist: {}", optionalArtist.get());
            }
        } else log.info("No artist found with id: {}", artistId);
        return events;
    }

    public List<Event> getAllByDate(LocalDate date) {
        List<Event> events;
        events = eventRepository.findAllByBeginDateOrderByBeginTimeAsc(date);
        if (events.isEmpty()) {
            log.info("No events found for the date: {}", date);
        }
        return events;
    }

    public List<Event> getAllByDateGenreCity(LocalDate date, Integer genreId, String city) {
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);
        List<Event> events = List.of();
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
//            events = eventRepository.findAllByDateAndGenreAndCity(date, genre, city);
            if (events.isEmpty()) {
                log.info("No events found by query with parameters: {}, {}, {}", date, genre, city);
            }
        } else log.info("No genre found with id: {}", genreId);
        return events;
    }

    @Transactional
    public Event create(EventInDTO dto) {
        Event event = new Event();

        setData(event, dto);
        return eventRepository.save(event);
    }

    @Transactional
    public Event update(EventInDTO dto) {
        Event event = eventRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_EVENT_MESSAGE + dto.getId()));

        setData(event, dto);
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new CommonAppException(NO_EVENT_MESSAGE + id);
        } else eventRepository.deleteById(id);
    }

    public EventInDTO toInDTO(Event event) {
        return Optional.ofNullable(event)
                .map(entity -> mapper.convertValue(entity, EventInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public EventOutDTO toOutDTO(Event event) {
        return Optional.ofNullable(event)
                .map(entity -> mapper.convertValue(entity, EventOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    private void setData(Event event, EventInDTO dto) {
        event.setName(dto.getName());
        event.setBeginDate(dto.getBeginDate());
        event.setBeginTime(dto.getBeginTime());

        Venue venue = venueRepository.findById(dto.getVenueId())
                .orElseThrow(() -> new CommonAppException("No venue found with id: " + dto.getVenueId()));
        event.setVenue(venue);

        List<Artist> artistList = artistRepository.findAllById(dto.getArtistIdList())
                .orElseThrow(() -> new CommonAppException("No artists found with ids: " + dto.getArtistIdList()));
        event.setArtistList(artistList);

        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new CommonAppException("No genre found with id: " + dto.getGenreId()));
        event.setGenre(genre);

        List<Subgenre> subgenreList = subgenreRepository.findAllById(dto.getSubgenreIdList())
                .orElseThrow(() -> new CommonAppException("No subgenres found with ids: " + dto.getSubgenreIdList()));
        event.setSubgenreList(subgenreList);

        if (ticketRepository.findAllById(dto.getTicketIdList()).isEmpty()) {
            log.info("No tickets found with ids: " + dto.getTicketIdList());
            event.setTicketList(List.of());
        } else {
            List<Ticket> ticketList = ticketRepository.findAllById(dto.getTicketIdList()).get();
            event.setTicketList(ticketList);
        }
    }
}
