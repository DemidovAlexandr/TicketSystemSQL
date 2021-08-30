package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Genre;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.services.ArtistService;
import com.demidov.ticketsystemsql.services.GenreService;
import com.demidov.ticketsystemsql.services.SubgenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataInitializer {

    private final ArtistService artistService;
    private final GenreService genreService;
    private final SubgenreService subgenreService;

    public DataInitializer(ArtistService artistService, GenreService genreService, SubgenreService subgenreService) {
        this.artistService = artistService;
        this.genreService = genreService;
        this.subgenreService = subgenreService;
    }

    public void initData(){
        initGenre();
        initSubgenre();
        initArtist();
    }

    public void initGenre() {
        Genre genre = genreService.create("Concert");
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
}
