package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ArtistService artistService;
    private final GenreService genreService;
    private final SubgenreService subgenreService;
    private final UserService userService;
    private final TicketService ticketService;
    private final VenueService venueService;
    private final EventService eventService;
    private final PurchaseService purchaseService;

    public ValidDTO validDTO;

    public void initData() {
        initGenre();
        initSubgenre();
        initArtist();
        initUser();
        initVenue();
        initEvent();
        initTickets();
    }

    public void initGenre() {
        Genre genre = genreService.create(validDTO.getGenreInDTO());
        log.info("Created genre: {}", genre);
    }

    public void initSubgenre() {
        Subgenre subgenre = subgenreService.create(validDTO.getSubgenreInDTO());
        log.info("Created subgenre: {}", subgenre);
    }

    public void initArtist() {
        Artist artist = artistService.create(validDTO.getArtistInDTO());
        log.info("Created Artist: {}", artist);
    }

    public void initUser() {
        User user = userService.create(validDTO.getUserInDTO());
        log.info("Created user: {}", user);
    }

    public void initVenue() {
        Venue venue = venueService.create(validDTO.getVenueInDTO());
        log.info("Created venue: {}", venue);
    }

    public void initEvent() {
        Event event = eventService.create(validDTO.getEventInDTO());
        log.info("Created event: {}", event);
    }


    public void initTickets() {
        Ticket ticket = ticketService.create(validDTO.getTicketInDTO());
        log.info("Created ticket: {}", ticket);
    }

    public void initPurchase() {
        Purchase purchase = purchaseService.create(validDTO.getPurchaseInDTO());
        log.info("Created purchase: {}", purchase);
    }
}
