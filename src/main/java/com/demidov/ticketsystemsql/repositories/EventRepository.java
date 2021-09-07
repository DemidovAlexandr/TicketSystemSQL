package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Event;
import com.demidov.ticketsystemsql.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByArtistListContainingOrderByBeginDate(Artist artist);

    List<Event> findAllByBeginDateOrderByBeginTimeAsc(LocalDate date);

    @Query("select e from Event e where e.beginDate = ?1 and e.genre = ?2 and e.venue.city = ?3 order by e.beginTime")
    List<Event> findAllByDateAndGenreAndCity(LocalDate date, Genre genre, String cityName);

}
