package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Integer> {
    List<Venue> findAllByCityOrderByName(String city);
}
