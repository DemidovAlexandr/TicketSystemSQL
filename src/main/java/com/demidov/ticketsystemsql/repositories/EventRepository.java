package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByBeginDateOrderByBeginTimeAsc(LocalDate date);

    List<Event> findAllByVenue(Venue venue);

    List<Event> findAllByGenre(Genre genre);

    @Query("select e from Event e where :subgenre member of e.subgenreList order by e.beginDate")
    List<Event> findAllBySubgenre(@Param("subgenre") Subgenre subgenre);

    @Query("select e from Event e where :artist member of e.artistList order by e.beginDate")
    List<Event> findAllByArtist(@Param("artist") Artist artist);

    //date, date - genre, date - city, genre - city, date - venue & PERIOD

//    @Query("select e from Event e where e.beginDate = ?1 and e.genre = ?2 and e.venue.city = ?3 order by e.beginTime")
//    List<Event> findAllByDateAndGenreAndCity(LocalDate date, Genre genre, String cityName);

}
