package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final static String NO_EVENT_MESSAGE = "There is no such event with id: ";
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final SubgenreRepository subgenreRepository;
    private final VenueRepository venueRepository;
    private final TicketRepository ticketRepository;

    public Event getById(Integer id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else throw new CommonAppException(NO_EVENT_MESSAGE + id);
    }

    public List<Event> getAllByArtistList(List<Integer> artistIdList) {
        Optional<List<Artist>> optionalArtists = artistRepository.findAllById(artistIdList);
        if (optionalArtists.isPresent()) {
            Optional<List<Event>> optionalEvents = eventRepository.findAllByArtistList(optionalArtists.get());
            if (optionalEvents.isPresent()) {
                return optionalEvents.get();
            } else throw new CommonAppException("No events found with this artists: " + optionalArtists.get());
        } else throw new CommonAppException("No artists found with this ids: " + artistIdList);
    }

    public List<Event> getAllByDateGenreCity(ZonedDateTime dateTime, Integer genreId, String city) {
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
    public Event create(String name, ZonedDateTime beginDateTime, Integer venueId, List<Integer> artistIdList,
                        Integer genreId, List<Integer> subgenreIdList, List<Integer> ticketIdList) {
        Event event = new Event();
        event.setName(name);

        event.setBeginDateTime(beginDateTime);

        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new CommonAppException("No venue found with id: " + venueId));
        event.setVenue(venue);

        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new CommonAppException("No genre found with id: " + genreId));
        event.setGenre(genre);

        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList)
                .orElseThrow(() -> new CommonAppException("No subgenres found with ids: " + subgenreIdList));
        event.setSubgenreList(subgenreList);

        List<Artist> artistList = artistRepository.findAllById(artistIdList).orElseThrow(() -> new CommonAppException("No artists found with ids: " + artistIdList));
        event.setArtistList(artistList);

        if (ticketRepository.findAllById(ticketIdList).isEmpty()) {
            log.info("No tickets found with ids: " + ticketIdList);
            event.setTicketList(null);
        } else {
            List<Ticket> ticketList = ticketRepository.findAllById(ticketIdList).get();
            event.setTicketList(ticketList);
        }
        return eventRepository.save(event);
    }

    @Transactional
    public Event update(Integer id, String name, ZonedDateTime beginDateTime, Integer venueId, List<Integer> artistIdList,
                        Integer genreId, List<Integer> subgenreIdList, List<Integer> ticketIdList) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new CommonAppException(NO_EVENT_MESSAGE + id));

        event.setName(name);
        event.setBeginDateTime(beginDateTime);

        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new CommonAppException("No venue found with id: " + venueId));
        event.setVenue(venue);

        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new CommonAppException("No genre found with id: " + genreId));
        event.setGenre(genre);

        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList)
                .orElseThrow(() -> new CommonAppException("No subgenres found with ids: " + subgenreIdList));
        event.setSubgenreList(subgenreList);

        List<Artist> artistList = artistRepository.findAllById(artistIdList).orElseThrow(() -> new CommonAppException("No artists found with ids: " + artistIdList));
        event.setArtistList(artistList);

        if (ticketRepository.findAllById(ticketIdList).isEmpty()) {
            log.info("No tickets found with ids: " + ticketIdList);
            event.setTicketList(null);
        } else {
            List<Ticket> ticketList = ticketRepository.findAllById(ticketIdList).get();
            event.setTicketList(ticketList);
        }
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new CommonAppException(NO_EVENT_MESSAGE + id);
        } else eventRepository.deleteById(id);
    }
}
