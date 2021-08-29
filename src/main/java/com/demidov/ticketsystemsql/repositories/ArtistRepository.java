package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {


}
