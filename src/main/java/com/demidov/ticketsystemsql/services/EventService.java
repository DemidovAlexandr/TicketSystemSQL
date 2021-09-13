package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final EntityManager entityManager;

    public Event getById(Integer id, boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedEventFilter");
        filter.setParameter("isDeleted", isDeleted);

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            session.disableFilter("deletedEventFilter");
            return optionalEvent.get();
        } else {
            session.disableFilter("deletedEventFilter");
            throw new CommonAppException(NO_EVENT_MESSAGE + id);
        }
    }

    public List<Event> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedEventFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<Event> events = eventRepository.findAll();
        session.disableFilter("deletedEventFilter");
        return events;
    }

    public List<Event> getAllByArtist(Integer artistId, boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedEventFilter");
        filter.setParameter("isDeleted", isDeleted);

        Optional<Artist> optionalArtist = artistRepository.findById(artistId);
        List<Event> events = new ArrayList<>();
        if (optionalArtist.isPresent()) {
            events = eventRepository.findAllByArtist(optionalArtist.get());
            if (events.isEmpty()) {
                log.info("No events found with this artist: {}", optionalArtist.get());
            }
        } else log.info("No artist found with id: {}", artistId);
        session.disableFilter("deletedEventFilter");
        return events;
    }

    public List<Event> getAllByDate(LocalDate date, boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedEventFilter");
        filter.setParameter("isDeleted", isDeleted);

        List<Event> events = eventRepository.findAllByBeginDateOrderByBeginTimeAsc(date);
        if (events.isEmpty()) {
            log.info("No events found for the date: {}", date);
        }
        session.disableFilter("deletedEventFilter");
        return events;
    }

    public List<Event> getAllByDateGenreCity(LocalDate date, Integer genreId, String city, boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedEventFilter");
        filter.setParameter("isDeleted", isDeleted);

        Optional<Genre> optionalGenre = genreRepository.findById(genreId);
        List<Event> events = new ArrayList<>();

        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
//            events = eventRepository.findAllByDateAndGenreAndCity(date, genre, city);
            if (events.isEmpty()) {
                log.info("No events found by query with parameters: {}, {}, {}", date, genre, city);
            }
        } else log.info("No genre found with id: {}", genreId);
        session.disableFilter("deletedEventFilter");
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
        } else {
            List<Ticket> ticketList = ticketRepository.findAllById(dto.getTicketIdList());
            event.setTicketList(ticketList);
        }
    }
}
