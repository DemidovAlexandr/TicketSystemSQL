package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.ArtistInDTO;
import com.demidov.ticketsystemsql.dto.out.ArtistOutDTO;
import com.demidov.ticketsystemsql.services.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistWebService {

    private final ArtistService artistService;

    public ArtistOutDTO getById(Integer id) {
        return artistService.toOutDTO(artistService.getById(id));
    }

    public List<ArtistOutDTO> getAll() {
        return artistService.getAll().stream()
                .map(artistService::toOutDTO)
                .collect(Collectors.toList());
    }

    public ArtistOutDTO create(ArtistInDTO dto) {
        return artistService.toOutDTO(artistService.create(
                dto.getName(),
                dto.getSubgenreIdList()
        ));
    }

    public ArtistOutDTO update(ArtistInDTO dto) {
        return artistService.toOutDTO(artistService.update(
                dto.getId(),
                dto.getName(),
                dto.getSubgenreIdList()
        ));
    }

    public void deleteById(Integer id){
        artistService.deleteById(id);
    }
}
