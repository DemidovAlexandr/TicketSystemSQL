package com.demidov.ticketsystemsql;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.services.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Genre genre = genreService.getById(1);
        Assertions.assertEquals(genre.getName(), "Concert");
    }

    @Test
    public void testIfGenreHasSubgenreList() {
        List<Subgenre> subgenreList = new ArrayList<>();
        subgenreList.add(subgenreService.getById(1));
        subgenreList.add(subgenreService.getById(2));
        Genre genre = genreService.getById(1);
        assert genre.getSubgenreList() != null;
        log.info("Genre with ID 1 is: " + genre + genre.getSubgenreList().toString());
        Assertions.assertEquals(genre.getSubgenreList().toString(), subgenreList.toString());
    }

    @Test
    public void testIfArtistHasSubgenreList() {
        List<Subgenre> subgenreList = new ArrayList<>();
        subgenreList.add(subgenreService.getById(1));
        subgenreList.add(subgenreService.getById(2));
        Artist artist = artistService.getById(1);
        Assertions.assertEquals(artist.getSubgenreList().toString(), subgenreList.toString());
    }

    @Test
    public void testIfSubgenresHaveGenre() {
        Subgenre subgenre1 = subgenreService.getById(1);
        Subgenre subgenre2 = subgenreService.getById(2);
        Assertions.assertEquals(subgenre1.getGenre(), genreService.getById(1));
        Assertions.assertEquals(subgenre2.getGenre(), genreService.getById(1));
    }

    @Test
    public void testIfVenueInitialized() {
        Venue venue = venueService.getById(1);
        Assertions.assertEquals("Aurora Concert Hall", venue.getName());
    }

    @Test
    public void testIfUserInitialized() {
        User user = userService.getById(1);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Assertions.assertEquals("29.10.1993", format.format(user.getDateOfBirth()));
    }

    @Test
    public void testIfEventInitialized() {
        Event event = eventService.getById(1);
        Assertions.assertEquals("Концерт группы Pantera", event.getName());
    }

    @Test
    public void testIfTicketsInitialized() {
        Random random = new Random();
        Integer id = random.nextInt(99)+1;
        Ticket ticket = ticketService.getById(id);
        Assertions.assertEquals(3000, ticket.getPrice());
        Assertions.assertEquals("Концерт группы Pantera", ticket.getEvent().getName());
    }
}
