package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Query("select a from Artist a where a.id in :idList")
    Optional<List<Artist>> findAllById(List<Integer> idList);
}
