package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Subgenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<List<Artist>> getAllBySubgenreListOrderByNameAsc(List<Subgenre> subgenreList);
}
