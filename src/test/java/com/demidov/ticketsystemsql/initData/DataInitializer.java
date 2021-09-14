package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.services.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class DataInitializer {

    private final ArtistService artistService;
    private final GenreService genreService;
    private final SubgenreService subgenreService;
    private final UserService userService;
    private final TicketService ticketService;
    private final VenueService venueService;
    private final EventService eventService;
    private final PurchaseService purchaseService;

    private final ValidDTO validDTO;

    private Genre genre;
    private Subgenre subgenre;
    private Artist artist;
    private User user;
    private Venue venue;
    private Event event;
    private Ticket ticket;
    private Purchase purchase;
    private User deletedUser;
    private Ticket availableTicket;
    private Event deletedEvent;
    private Ticket deletedEventTicket;
    private Genre movieGenre;
    private Subgenre actionSubgenre;
    private Subgenre comedySubgenre;
    private Artist movieArtist;
    private Artist anotherMovieArtist;
    private Venue cinemaVenue;
    private Event movieEvent;
    private Ticket movieTicket;

    public void initData() {
        initGenre();
        initSubgenre();
        initArtist();
        initUser();
        initVenue();
        initEvent();
        initTicket();
        initPurchase();
        initDeletedUser();
        initAvailableTicket();
        initDeletedEvent();
        initDeletedEventTicket();
        initMovieGenre();
        initActionSubgenre();
        initComedySubgenre();
        initMovieArtist();
        initAnotherMovieArtist();
        initCinemaVenue();
        initMovieEvent();
        initMovieTicket();
    }

    @Transactional
    public void initGenre() {
        Genre genre = genreService.create(validDTO.getGenreInDTO());
        log.info("Created genre: {}", genre);
        this.genre = genre;
    }

    @Transactional
    public void initSubgenre() {
        Subgenre subgenre = subgenreService.create(validDTO.getSubgenreInDTO());
        log.info("Created subgenre: {}", subgenre);
        this.subgenre = subgenre;
    }

    @Transactional
    public void initArtist() {
        Artist artist = artistService.create(validDTO.getArtistInDTO());
        log.info("Created Artist: {}", artist);
        this.artist = artist;
    }

    @Transactional
    public void initUser() {
        User user = userService.create(validDTO.getUserInDTO());
        log.info("Created user: {}", user);
        this.user = user;
    }

    @Transactional
    public void initVenue() {
        Venue venue = venueService.create(validDTO.getVenueInDTO());
        log.info("Created venue: {}", venue);
        this.venue = venue;
    }

    @Transactional
    public void initEvent() {
        Event event = eventService.create(validDTO.getEventInDTO());
        log.info("Created event: {}", event);
        this.event = event;
    }

    @Transactional
    public void initTicket() {
        Ticket ticket = ticketService.create(validDTO.getTicketInDTO());
        log.info("Created ticket: {}", ticket);
        this.ticket = ticket;
    }

    @Transactional
    public void initPurchase() {
        Purchase purchase = purchaseService.create(validDTO.getPurchaseInDTO());
        log.info("Created purchase: {}", purchase);
        this.purchase = purchase;
    }

    @Transactional
    public void initDeletedUser() {
        User user = userService.create(validDTO.getDeletedUserInDto());
        log.info("Created deleted user: {}", user);
        this.deletedUser = user;
    }

    @Transactional
    public void initAvailableTicket() {
        Ticket ticket = ticketService.create(validDTO.getAvailableTicket());
        log.info("Created available ticket: {}", ticket);
        this.availableTicket = ticket;
    }

    @Transactional
    public void initDeletedEvent() {
        Event event = eventService.create(validDTO.getDeletedEventDTO());
        log.info("Created deleted event: {}", event);
        this.deletedEvent = event;
    }

    @Transactional
    public void initDeletedEventTicket() {
        Ticket ticket = ticketService.create(validDTO.getDeletedEventTicketDTO());
        log.info("Created deleted event ticket: {}", ticket);
        this.deletedEventTicket = ticket;
    }

    @Transactional
    public void initMovieGenre() {
        Genre genre = genreService.create(validDTO.getMovieGenreInDTO());
        log.info("Created genre: {}", genre);
        this.movieGenre = genre;
    }

    @Transactional
    public void initActionSubgenre() {
        Subgenre subgenre = subgenreService.create(validDTO.getActionSubgenreInDTO());
        log.info("Created subgenre: {}", subgenre);
        this.actionSubgenre = subgenre;
    }

    @Transactional
    public void initComedySubgenre() {
        Subgenre subgenre = subgenreService.create(validDTO.getComedySubgenreInDTO());
        log.info("Created subgenre: {}", subgenre);
        this.comedySubgenre = subgenre;
    }

    @Transactional
    public void initMovieArtist() {
        Artist artist = artistService.create(validDTO.getMovieArtistDTO());
        log.info("Created Artist: {}", artist);
        this.movieArtist = artist;
    }

    @Transactional
    public void initAnotherMovieArtist() {
        Artist artist = artistService.create(validDTO.getAnotherMovieArtistDTO());
        log.info("Created Artist: {}", artist);
        this.anotherMovieArtist = artist;
    }

    @Transactional
    public void initCinemaVenue() {
        Venue venue = venueService.create(validDTO.getCinemaVenueDTO());
        log.info("Created venue: {}", venue);
        this.cinemaVenue = venue;
    }

    @Transactional
    public void initMovieEvent() {
        Event event = eventService.create(validDTO.getMovieEventInDTO());
        log.info("Created event: {}", event);
        this.movieEvent = event;
    }

    @Transactional
    public void initMovieTicket() {
        Ticket ticket = ticketService.create(validDTO.getMovieTicketDTO());
        log.info("Created ticket: {}", ticket);
        this.movieTicket = ticket;
    }
}
