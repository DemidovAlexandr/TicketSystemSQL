package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.GenreInDTO;
import com.demidov.ticketsystemsql.dto.out.GenreOutDTO;
import com.demidov.ticketsystemsql.entities.Genre;
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
public class GenreService {

    private static final String NO_GENRE_MESSAGE = "There is no such genre with id: ";
    private static final String GENRE_EXISTS = "Genre with such name already exists, id: ";
    private static final String DTO_IS_NULL = "DTO must not be null";

    private final GenreRepository genreRepository;
    private final SubgenreRepository subgenreRepository;
    private final ObjectMapper mapper;


    public Genre getById(Integer id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return genre.get();
        } else throw new CommonAppException(NO_GENRE_MESSAGE + id);
    }

    public Genre getByName(String name) {
        Optional<Genre> genre = genreRepository.findByNameAllIgnoreCase(name);
        if (genre.isPresent()) {
            return genre.get();
        } else throw new CommonAppException("There is no such genre with name: " + name);
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }


    @Transactional
    public Genre create(GenreInDTO dto) {

        if (genreRepository.findByNameAllIgnoreCase(dto.getName()).isPresent()) {
            throw new CommonAppException(GENRE_EXISTS + genreRepository.findByNameAllIgnoreCase(dto.getName()).get().getId());
        } else {
            Genre genre = new Genre();
            setData(genre, dto);
            return genreRepository.save(genre);
        }
    }

    @Transactional
    public Genre update(GenreInDTO dto) {
        Genre genre = genreRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_GENRE_MESSAGE + dto.getId()));
        setData(genre, dto);
        return genreRepository.save(genre);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!genreRepository.existsById(id)) {
            throw new CommonAppException(NO_GENRE_MESSAGE + id);
        } else genreRepository.deleteById(id);
    }

    public GenreInDTO toInDTO(Genre genre) {
        return Optional.ofNullable(genre)
                .map(entity -> mapper.convertValue(entity, GenreInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public GenreOutDTO toOutDTO(Genre genre) {
        return Optional.ofNullable(genre)
                .map(entity -> mapper.convertValue(entity, GenreOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    private void setData(Genre genre, GenreInDTO dto) {
        genre.setName(dto.getName());
        List<Subgenre> subgenreList = subgenreRepository.findAllById(dto.getSubgenreIdList()).orElse(List.of());
        genre.setSubgenreList(subgenreList);
    }
}
