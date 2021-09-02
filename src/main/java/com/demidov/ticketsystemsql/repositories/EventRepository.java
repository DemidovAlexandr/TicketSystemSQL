package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Event;
import com.demidov.ticketsystemsql.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<List<Event>> findAllByArtistListContainingOrderByBeginDateTime(Artist artist);

    Optional<List<Event>> findAllByBeginDateTimeOrderByNameAsc(LocalDateTime dateTime);

    @Query("select e from Event e where e.beginDateTime = ?1 and e.genre = ?2 and e.venue.city = ?3 order by e.name")
    Optional<List<Event>> findAllByDateAndGenreAndCity(LocalDateTime dateTime, Genre genre, String cityName);
    //TODO get rid of optional


}
