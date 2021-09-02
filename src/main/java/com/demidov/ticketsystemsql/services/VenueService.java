package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.dto.out.VenueOutDTO;
import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.entities.Venue;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.VenueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final static String NO_VENUE_MESSAGE = "There is no such venue with id: ";
    private final static String DTO_IS_NULL = "DTO must not be null";

    private final VenueRepository venueRepository;
    private final ObjectMapper mapper;

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
    public Venue create(VenueInDTO dto) {
        Venue venue = new Venue();
        setData(venue, dto);
        return venueRepository.save(venue);
    }

    @Transactional
    public Venue update(VenueInDTO dto) {
        Venue venue = venueRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_VENUE_MESSAGE + dto.getId()));
        setData(venue, dto);
        return venueRepository.save(venue);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!venueRepository.existsById(id)) {
            throw new CommonAppException(NO_VENUE_MESSAGE + id);
        } else venueRepository.deleteById(id);
    }

    public VenueInDTO toInDTO(Venue venue) {
        return Optional.ofNullable(venue)
                .map(entity -> mapper.convertValue(entity, VenueInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public VenueOutDTO toOutDTO(Venue venue) {
        return Optional.ofNullable(venue)
                .map(entity -> mapper.convertValue(entity, VenueOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    private void setData(Venue venue, VenueInDTO dto) {
        venue.setName(dto.getName());
        venue.setCity(dto.getCity());
        venue.setStreetAddress(dto.getStreetAddress());
        venue.setDescription(dto.getDescription());
        venue.setContacts(dto.getContacts());
    }
}
