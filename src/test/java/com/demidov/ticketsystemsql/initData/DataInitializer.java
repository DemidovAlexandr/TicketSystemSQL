package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.services.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public User validUser;

    public void initData(){
        initGenre();
        initSubgenre();
        initArtist();
        initUser();
        initVenue();
        initEvent();
        initTickets();
    }

    public void initGenre() {
        Genre genre = genreService.create("Concert", List.of());
        log.info("Created genre: {}", genre);
    }

    public void initSubgenre() {
        Subgenre metal = subgenreService.create("Metal", 1);
        log.info("Created subgenre: {}", metal);
        Subgenre rock = subgenreService.create("Rock", 1);
        log.info("Created subgenre: {}", rock);
    }

    public void initArtist() {
        List<Integer> subgenreIdList = new ArrayList<>();
        subgenreIdList.add(1);
        subgenreIdList.add(2);
        Artist artist = artistService.create("Pantera", subgenreIdList);
        log.info("Created Artist: {}", artist);
    }

    public void initUser() {
        LocalDate dateOfBirth = LocalDate.of(1993, 10,29);
        validUser = userService.create("Иван", "Петров", dateOfBirth, "+79991234567", "usermail@user.com", "Санкт-Петербург");
        log.info("Created user: {}", validUser);
    }

    public void initVenue() {
        Venue venue = venueService.create("Aurora Concert Hall", "Санкт-Петербург", "Пироговская наб., 5/2",
                "Современный клуб и концертный зал, где выступают российские и зарубежные рок-артисты",
                "Телефон: 8 (812) 907-19-17");
        log.info("Created venue: {}", venue);
    }

    public void initEvent() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 10, 15, 20, 0);
        List<Integer> artistList = new ArrayList<>();
        artistList.add(1);
        List<Integer> subgenreList = new ArrayList<>();
        subgenreList.add(1);
        subgenreList.add(2);
        List<Integer> ticketList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            ticketList.add(i);
        }
        Event event = eventService.create("Концерт группы Pantera", dateTime, 1, artistList, 1,  subgenreList, ticketList);
        log.info("Created event {}", event);
    }


    public void initTickets() {
        int rows = 10;
        int seats = 10;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= seats ; j++) {
                Ticket ticket = ticketService.create(i, j, 3000, 1);
            }
        }
    }
}
