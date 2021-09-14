package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

//    @Query("select e from Event e where e.beginDate between :fromDate and :toDate order by e.beginDate, e.beginTime")
//    List<Event> findAllByDate(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    //Controller methods:

    @Query("select e from Event e where e.beginDate between :fromDate and :toDate " +
            "and e.venue.city = :city order by e.beginDate, e.beginTime")
    List<Event> findAllByDate(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, @Param("city") String city);

    @Query("select e from Event e where e.beginDate between :fromDate and :toDate " +
            "and e.venue.city = :city and e.genre.id = :genreId order by e.beginDate, e.beginTime")
    List<Event> findAllByDateAndGenre(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
                                             @Param("city") String city, @Param("genreId") Integer genreId);

    @Query("select e from Event e where e.beginDate between :fromDate and :toDate " +
            "and e.venue.city = :city and e.genre.id = :genreId and :subgenre member of e.subgenreList order by e.beginDate, e.beginTime")
    List<Event> findAllByDateAndGenreAndSubgenre(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
                                         @Param("city") String city,
                                         @Param("genreId") Integer genreId, @Param("subgenre") Subgenre subgenre);

    @Query("select e from Event e where e.beginDate between :fromDate and :toDate " +
            "and :artist member of e.artistList order by e.beginDate, e.beginTime")
    List<Event> findAllByArtist(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
                                @Param("artist") Artist artist);

    @Query("select e from Event e where e.beginDate between :fromDate and :toDate " +
            "and e.venue = :venue order by e.beginDate, e.beginTime")
    List<Event> findAllByVenue(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
                               @Param("venue") Venue venue);

    //Service methods:

    @Query("select e from Event e where :artist member of e.artistList order by e.beginDate, e.beginTime")
    List<Event> findAllByArtist(@Param("artist") Artist artist);

    @Query("select e from Event e where e.venue = :venue order by e.beginDate, e.beginTime")
    List<Event> findAllByVenue(@Param("venue") Venue venue);

    @Query("select e from Event e where e.genre = :genre order by e.beginDate, e.beginTime")
    List<Event> findAllByGenre(@Param("genre") Genre genre);

    @Query("select e from Event e where :subgenre member of e.subgenreList order by e.beginDate")
    List<Event> findAllBySubgenre(@Param("subgenre") Subgenre subgenre);

}
