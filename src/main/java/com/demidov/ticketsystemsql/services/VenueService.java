package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.Venue;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final static String NO_VENUE_MESSAGE = "There is no such venue with id: ";

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public Venue getById(Integer id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isPresent()) {
            return venue.get();
        } else throw new CommonAppException(NO_VENUE_MESSAGE + id);
    }

    public List<Venue> getAll() {
        return venueRepository.findAll();
    }

    public List<Venue> getAllByCity(String city) {
        Optional<List<Venue>> venueList = venueRepository.findAllByCityOrderByName(city);
        if (venueList.isPresent()) {
            return venueList.get();
        } else throw new CommonAppException("No venues found in the city: " + city);
    }

    @Transactional
    public Venue create(String name, String city, String streetAddress, String description, String contacts) {
        Venue venue = new Venue();
        venue.setName(name);
        venue.setCity(city);
        venue.setStreetAddress(streetAddress);
        venue.setDescription(description);
        venue.setContacts(contacts);
        return venueRepository.save(venue);
    }

    @Transactional
    public Venue update(Integer id, String name, String city, String streetAddress, String description, String contacts) {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new CommonAppException(NO_VENUE_MESSAGE + id));
        venue.setName(name);
        venue.setCity(city);
        venue.setStreetAddress(streetAddress);
        venue.setDescription(description);
        venue.setContacts(contacts);
        return venueRepository.save(venue);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!venueRepository.existsById(id)) {
            throw new CommonAppException(NO_VENUE_MESSAGE + id);
        } else venueRepository.deleteById(id);
    }
}
