package com.demidov.ticketsystemsql;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Genre;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.services.ArtistService;
import com.demidov.ticketsystemsql.services.GenreService;
import com.demidov.ticketsystemsql.services.SubgenreService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private DataInitializer dataInitializer;

    private boolean isInitialized;

//    @Autowired
//    EntityManager entityManager;
//
//    String artistName = "Children Of Bodom";
//    String artistName2 = "Foals";
//    String genreName = "Concert";
//    String subgenreName = "Metal";
//    String subgenreName1 = "Rock";
//    String subgenreName2 = "Classical music";
//    String subgenreName3 = "Indie";

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
}
