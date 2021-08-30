package com.demidov.ticketsystemsql;

import com.demidov.ticketsystemsql.entities.Genre;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.services.ArtistService;
import com.demidov.ticketsystemsql.services.GenreService;
import com.demidov.ticketsystemsql.services.SubgenreService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServicesTests {

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
        Genre genre = genreService.getById(1);
        Assertions.assertNotNull(genre.getSubgenreList());
    }
}
