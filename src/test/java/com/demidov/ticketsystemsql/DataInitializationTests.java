package com.demidov.ticketsystemsql;

import com.demidov.ticketsystemsql.entities.*;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.services.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
        assertEquals(genre.getName(), genreService.getById(genre.getId()).getName());
    }

    @Test
    public void testSubgenreCreation() {
        Subgenre subgenre = dataInitializer.getSubgenre();
        assertEquals(subgenre.getName(), subgenreService.getById(subgenre.getId()).getName());
    }

    @Test
    public void testArtistCreation() {
        Artist artist = dataInitializer.getArtist();
        assertEquals(artist.getName(), artistService.getById(artist.getId()).getName());
    }

    @Test
    public void testVenueCreation() {
        Venue venue = dataInitializer.getVenue();
        assertEquals(venue.getName(), venueService.getById(venue.getId()).getName());
    }

    @Test
    public void testUserCreation() {
        User user = dataInitializer.getUser();
        assertEquals(user.getDateOfBirth(), userService.getById(user.getId(), false).getDateOfBirth());
        assertEquals(user.getName(), userService.getById(user.getId(), false).getName());
    }

    @Test
    public void testEventCreation() {
        Event event = dataInitializer.getEvent();
        Event actualEvent = eventService.getById(event.getId(), false);
        assertEquals(event.getName(), actualEvent.getName());
        assertEquals(event.getBeginDate(), actualEvent.getBeginDate());
        assertEquals(event.getBeginTime(), actualEvent.getBeginTime());
        assertEquals(event.getGenre().toString(), actualEvent.getGenre().toString());
        assertEquals(event.getSubgenreList().toString(), actualEvent.getSubgenreList().toString());
        assertEquals(event.getArtistList().toString(), actualEvent.getArtistList().toString());
        assert event.getTicketList() != null;
        assert actualEvent.getTicketList() != null;
        assertEquals(List.of(ticketService.getById(dataInitializer.getTicket().getId()),
                ticketService.getById(dataInitializer.getAvailableTicket().getId())).toString(),
                actualEvent.getTicketList().toString());
        assertEquals(event.getVenue().toString(), actualEvent.getVenue().toString());
    }

    @Test
    public void testTicketCreation() {
        Ticket ticket = dataInitializer.getTicket();
        Assertions.assertEquals(ticket.getPrice(), ticketService.getById(ticket.getId()).getPrice());
        Assertions.assertEquals(ticket.getEvent().getName(), ticketService.getById(ticket.getId()).getEvent().getName());
    }

    @Test
    public void testIfEventsFoundByArtist() {
        Artist artist = dataInitializer.getArtist();
        Event event = dataInitializer.getEvent();
        List<Event> events = eventService.getAllByArtist(artist.getId(), false);
        Assertions.assertEquals(events.size(), 1);
        Assertions.assertEquals(event.getName(), events.get(0).getName());
    }

    @Test
    public void testPurchaseCreation() {
        Purchase purchase = dataInitializer.getPurchase();
        User user = dataInitializer.getUser();
        Ticket ticket = dataInitializer.getTicket();
        Assertions.assertEquals(user.toString(), purchaseService.getById(purchase.getId()).getUser().toString());
        Assertions.assertEquals(ticket.getPrice(), purchaseService.getById(purchase.getId()).getTotal());
    }

    @Test
    public void testDeletedUserCreation() {
        User user = dataInitializer.getDeletedUser();
        Assertions.assertEquals(user.toString(), userService.getById(user.getId(), user.isDeleted()).toString());
    }

    @Test
    public void testAvailableTicketCreation() {
        Ticket ticket = dataInitializer.getAvailableTicket();
        Assertions.assertEquals(ticket.getPrice(), ticketService.getById(ticket.getId()).getPrice());
        Assertions.assertEquals(ticket.getEvent().getName(), ticketService.getById(ticket.getId()).getEvent().getName());
    }

    @Test
    public void testDeletedEventCreation() {
        Event event = dataInitializer.getDeletedEvent();
        Event actualEvent = eventService.getById(event.getId(), true);
        assertEquals(event.getName(), actualEvent.getName());
        assertEquals(event.getBeginDate(), actualEvent.getBeginDate());
        assertEquals(event.getBeginTime(), actualEvent.getBeginTime());
        assertEquals(event.getGenre().toString(), actualEvent.getGenre().toString());
        assertEquals(event.getSubgenreList().toString(), actualEvent.getSubgenreList().toString());
        assertEquals(event.getArtistList().toString(), actualEvent.getArtistList().toString());
        assert event.getTicketList() != null;
        assert actualEvent.getTicketList() != null;
        assertEquals(List.of(ticketService.getById(dataInitializer.getDeletedEventTicket().getId())).toString(),
                actualEvent.getTicketList().toString());
        assertEquals(event.getVenue().toString(), actualEvent.getVenue().toString());
    }

    @Test
    public void testDeletedEventTicketCreation() {
        Ticket ticket = dataInitializer.getDeletedEventTicket();
        Assertions.assertEquals(ticket.getPrice(), ticketService.getById(ticket.getId()).getPrice());
        Assertions.assertEquals(ticket.getEvent().getName(), ticketService.getById(ticket.getId()).getEvent().getName());
    }

    @Test
    public void testMovieGenreCreation() {
        Genre genre = dataInitializer.getMovieGenre();
        assertEquals(genre.getName(), genreService.getById(genre.getId()).getName());
    }

    @Test
    public void testActionSubgenreCreation() {
        Subgenre subgenre = dataInitializer.getActionSubgenre();
        assertEquals(subgenre.getName(), subgenreService.getById(subgenre.getId()).getName());
    }

    @Test
    public void testComedySubgenreCreation() {
        Subgenre subgenre = dataInitializer.getComedySubgenre();
        assertEquals(subgenre.getName(), subgenreService.getById(subgenre.getId()).getName());
    }

    @Test
    public void testMovieArtistCreation() {
        Artist artist = dataInitializer.getMovieArtist();
        assertEquals(artist.getName(), artistService.getById(artist.getId()).getName());
    }

    @Test
    public void testAnotherMovieArtistCreation() {
        Artist artist = dataInitializer.getAnotherMovieArtist();
        assertEquals(artist.getName(), artistService.getById(artist.getId()).getName());
    }

    @Test
    public void testCinemaVenueCreation() {
        Venue venue = dataInitializer.getCinemaVenue();
        assertEquals(venue.getName(), venueService.getById(venue.getId()).getName());
    }

    @Test
    public void testMovieEventCreation() {
        Event event = dataInitializer.getMovieEvent();
        Event actualEvent = eventService.getById(event.getId(), false);
        assertEquals(event.getName(), actualEvent.getName());
        assertEquals(event.getBeginDate(), actualEvent.getBeginDate());
        assertEquals(event.getBeginTime(), actualEvent.getBeginTime());
        assertEquals(event.getGenre().toString(), actualEvent.getGenre().toString());
        assertEquals(event.getSubgenreList().toString(), actualEvent.getSubgenreList().toString());
        assertEquals(event.getArtistList().toString(), actualEvent.getArtistList().toString());
        assert event.getTicketList() != null;
        assert actualEvent.getTicketList() != null;
        assertEquals(List.of(ticketService.getById(dataInitializer.getMovieTicket().getId())).toString(),
                actualEvent.getTicketList().toString());
        assertEquals(event.getVenue().toString(), actualEvent.getVenue().toString());
    }

    @Test
    public void testMovieTicketCreation() {
        Ticket ticket = dataInitializer.getMovieTicket();
        Assertions.assertEquals(ticket.getPrice(), ticketService.getById(ticket.getId()).getPrice());
        Assertions.assertEquals(ticket.getEvent().getName(), ticketService.getById(ticket.getId()).getEvent().getName());
    }
}
