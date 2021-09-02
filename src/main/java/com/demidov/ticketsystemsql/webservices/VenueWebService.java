package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.dto.out.VenueOutDTO;
import com.demidov.ticketsystemsql.services.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueWebService {

    private final VenueService venueService;

    public VenueOutDTO getById(Integer id) {
       return venueService.toOutDTO(venueService.getById(id));
    }

    public List<VenueOutDTO> getAll() {
        return venueService.getAll()
                .stream()
                .map(venueService::toOutDTO)
                .collect(Collectors.toList());
    }

    public VenueOutDTO create(VenueInDTO dto) {
        return venueService.toOutDTO(venueService.create(
                dto.getName(),
                dto.getCity(),
                dto.getStreetAddress(),
                dto.getDescription(),
                dto.getContacts()
        ));
    }

    public VenueOutDTO update(VenueInDTO dto) {
        return venueService.toOutDTO(venueService.update(
                dto.getId(),
                dto.getName(),
                dto.getCity(),
                dto.getStreetAddress(),
                dto.getDescription(),
                dto.getContacts()
        ));
    }

    public void deleteById(Integer id) {
        venueService.deleteById(id);
    }
}
