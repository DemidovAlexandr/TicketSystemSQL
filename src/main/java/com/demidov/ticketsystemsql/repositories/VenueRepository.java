package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Integer> {

}
