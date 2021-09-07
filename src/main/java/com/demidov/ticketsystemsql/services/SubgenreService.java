package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.dto.out.SubgenreOutDTO;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.GenreRepository;
import com.demidov.ticketsystemsql.repositories.SubgenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubgenreService {

    private static final String NO_SUBGENRE_MESSAGE = "There is no such subgenre with id: ";
    private static final String SUBGENRE_EXISTS = "Subgenre with such name already exists, id: ";
    private static final String NO_GENRE_MESSAGE = "There is no such genre with id: ";
    private final static String DTO_IS_NULL = "DTO must not be null";

    private final SubgenreRepository subgenreRepository;
    private final GenreRepository genreRepository;
    private final ObjectMapper mapper;

    public Subgenre getById(Integer id) {
        Optional<Subgenre> subgenre = subgenreRepository.findById(id);
        if (subgenre.isPresent()) {
            return subgenre.get();
        } else throw new CommonAppException(NO_SUBGENRE_MESSAGE + id);
    }

    public List<Subgenre> getAll() {
        return subgenreRepository.findAll();
    }

    @Transactional
    public Subgenre create(SubgenreInDTO dto) {
        Subgenre subgenre = new Subgenre();
        setData(subgenre, dto);
        return subgenreRepository.save(subgenre);
    }

    @Transactional
    public Subgenre update(SubgenreInDTO dto) {
        Subgenre subgenre = subgenreRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_SUBGENRE_MESSAGE + dto.getId()));
        setData(subgenre, dto);
        return subgenreRepository.save(subgenre);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!subgenreRepository.existsById(id)) {
            throw new CommonAppException(NO_SUBGENRE_MESSAGE + id);
        } else subgenreRepository.deleteById(id);
    }

    public SubgenreInDTO toInDTO(Subgenre subgenre) {
        return Optional.ofNullable(subgenre)
                .map(entity -> mapper.convertValue(entity, SubgenreInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public SubgenreOutDTO toOutDTO(Subgenre subgenre) {
        return Optional.ofNullable(subgenre)
                .map(entity -> mapper.convertValue(entity, SubgenreOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    private void setData(Subgenre subgenre, SubgenreInDTO dto) {
        if (subgenreRepository.findByNameAllIgnoreCase(dto.getName()).isPresent()) {
            throw new CommonAppException(SUBGENRE_EXISTS + subgenreRepository.findByNameAllIgnoreCase(dto.getName()).get().getId());
        } else subgenre.setName(dto.getName());

        if (genreRepository.existsById(dto.getGenreId())) {
            subgenre.setGenre(genreRepository.getById(dto.getGenreId()));
        } else throw new CommonAppException(NO_GENRE_MESSAGE + dto.getGenreId());
    }
}
