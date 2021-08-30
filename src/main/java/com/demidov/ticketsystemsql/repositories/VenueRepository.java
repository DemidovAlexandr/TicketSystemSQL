package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Integer> {
    Optional<List<Venue>> findAllByCityOrderByName(String city);
}
