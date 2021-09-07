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


    public void initData() {
        initGenre();
        initSubgenre();
        initArtist();
        initUser();
        initVenue();
        initEvent();
        initTicket();
        initPurchase();
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

    private void initArtist() {
        Artist artist = artistService.create(validDTO.getArtistInDTO());
        log.info("Created Artist: {}", artist);
        this.artist = artist;
    }

    private void initUser() {
        User user = userService.create(validDTO.getUserInDTO());
        log.info("Created user: {}", user);
        this.user = user;
    }

    private void initVenue() {
        Venue venue = venueService.create(validDTO.getVenueInDTO());
        log.info("Created venue: {}", venue);
        this.venue = venue;
    }

    private void initEvent() {
        Event event = eventService.create(validDTO.getEventInDTO());
        log.info("Created event: {}", event);
        this.event = event;
    }

    private void initTicket() {
        Ticket ticket = ticketService.create(validDTO.getTicketInDTO());
        log.info("Created ticket: {}", ticket);
        this.ticket = ticket;
    }

    private void initPurchase() {
        Purchase purchase = purchaseService.create(validDTO.getPurchaseInDTO());
        log.info("Created purchase: {}", purchase);
        this.purchase = purchase;
    }
}
