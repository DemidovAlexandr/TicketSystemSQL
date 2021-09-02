package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.GenreInDTO;
import com.demidov.ticketsystemsql.dto.out.GenreOutDTO;
import com.demidov.ticketsystemsql.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreWebService {

    private final GenreService genreService;

    public GenreOutDTO getById(Integer id) {
        return genreService.toOutDTO(genreService.getById(id));
    }

    public GenreOutDTO getByName(String name) {
        return genreService.toOutDTO(genreService.getByName(name));
    }

    public List<GenreOutDTO> getAll() {
        return  genreService.getAll().stream()
                .map(genreService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public GenreOutDTO create(GenreInDTO dto) {
        return genreService.toOutDTO(genreService.create(
                dto.getName(),
                dto.getSubgenreIdList()
        ));
    }

    @Transactional
    public GenreOutDTO update(GenreInDTO dto) {
        return genreService.toOutDTO(genreService.update(
                dto.getId(),
                dto.getName(),
                dto.getSubgenreIdList()
        ));
    }

    @Transactional
    public void deleteById(Integer id) {
        genreService.deleteById(id);
    }
}
