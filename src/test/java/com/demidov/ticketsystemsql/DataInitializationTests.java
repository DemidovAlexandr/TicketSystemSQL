package com.demidov.ticketsystemsql;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.services.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class DataInitializationTests {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private SubgenreService subgenreService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private DataInitializer dataInitializer;

    private boolean isInitialized;

    @BeforeAll
    public void setUp() {
        if (!isInitialized) {
            dataInitializer.initData();
            isInitialized = true;
        }
    }

    @Test
    public void testGenreCreation() {
        Genre genre = dataInitializer.getGenre();
        Assertions.assertEquals(genreService.getById(genre.getId()).getName(), "Concert");
    }

    @Test
    public void testSubgenreCreation() {
        Subgenre subgenre = dataInitializer.getSubgenre();
        Genre genre = dataInitializer.getGenre();
        Assertions.assertEquals(Objects.requireNonNull(genreService.getById(genre.getId()).getSubgenreList()).toString(),
                subgenreService.getById(subgenre.getId()).toString());
    }

    @Test
    public void testIfArtistHasSubgenreList() {
        Artist artist = artistService.getById(dataInitializer.getArtist().getId());
        Assertions.assertEquals(artist.getSubgenreList().toString(),
                artistService.getById(artist.getId()).getSubgenreList().toString());
    }

    @Test
    public void testIfSubgenresHaveGenre() {
        Subgenre subgenre = dataInitializer.getSubgenre();
        Assertions.assertEquals(subgenre.getGenre().getName(),
                subgenreService.getById(subgenre.getId()).getGenre().getName());
    }

    @Test
    public void testIfVenueInitialized() {
        Venue venue = dataInitializer.getVenue();
        Assertions.assertEquals(venue.getName(), venueService.getById(venue.getId()).getName());
    }

    @Test
    public void testIfUserInitialized() {
        User user = dataInitializer.getUser();
        Assertions.assertEquals(user.getDateOfBirth(), userService.getById(user.getId()).getDateOfBirth());
    }

    @Test
    public void testIfEventInitialized() {
        Event event = dataInitializer.getEvent();
        Assertions.assertEquals(event.getName(), eventService.getById(event.getId()).getName());
    }

    @Test
    public void testIfTicketsInitialized() {
        Ticket ticket = dataInitializer.getTicket();
        Assertions.assertEquals(ticket.getPrice(), ticketService.getById(ticket.getId()).getPrice());
        Assertions.assertEquals(ticket.getEvent().getName(), ticketService.getById(ticket.getId()).getEvent().getName());
    }

    @Test
    public void testIfEventsFoundByArtist() {
        Artist artist = dataInitializer.getArtist();
        Event event = dataInitializer.getEvent();
        List<Event> events = eventService.getAllByArtist(artist.getId());
        Assertions.assertEquals(events.size(), 1);
        Assertions.assertEquals(event.getName(), events.get(0).getName());
    }
}
