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

import java.time.LocalDateTime;
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
        if (optionalArtist.isPresent()) {
            Optional<List<Event>> optionalEvents = eventRepository.findAllByArtistListContainingOrderByBeginDateTime(optionalArtist.get());
            if (optionalEvents.isPresent()) {
                return optionalEvents.get();
            } else throw new CommonAppException("No events found with this artists: " + optionalArtist.get());
        } else throw new CommonAppException("No artists found with this ids: " + artistId);
    }

    public List<Event> getAllByDateGenreCity(LocalDateTime dateTime, Integer genreId, String city) {
        Optional<Genre> optionalGenre = genreRepository.findById(genreId);
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            Optional<List<Event>> optionalEvents = eventRepository.findAllByDateAndGenreAndCity(dateTime, genre, city);
            if (optionalEvents.isPresent()) {
                return optionalEvents.get();
            } else throw new CommonAppException("No events found by query with parameters: " + dateTime + genre + city);
        } else throw new CommonAppException("No genre found with id: " + genreId);
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
        event.setBeginDateTime(dto.getBeginDateTime());

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
