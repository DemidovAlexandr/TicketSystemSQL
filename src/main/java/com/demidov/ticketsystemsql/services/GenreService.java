package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.Genre;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.GenreRepository;
import com.demidov.ticketsystemsql.repositories.SubgenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private static final String NO_GENRE_MESSAGE = "There is no such genre with id: ";
    private static final String GENRE_EXISTS = "Genre with such name already exists, id: ";
    private final GenreRepository genreRepository;
    private final SubgenreRepository subgenreRepository;

    public GenreService(GenreRepository genreRepository, SubgenreRepository subgenreRepository) {
        this.genreRepository = genreRepository;
        this.subgenreRepository = subgenreRepository;
    }

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
    public Genre create(String name) {

        if (genreRepository.findByNameAllIgnoreCase(name).isPresent()) {
            throw new CommonAppException(GENRE_EXISTS + genreRepository.findByNameAllIgnoreCase(name).get().getId());
        } else {
            Genre genre = new Genre();
            genre.setName(name);
            return genreRepository.save(genre);
        }
    }

    @Transactional
    public Genre create(String name, List<Integer> subgenreIdList) {

        if (genreRepository.findByNameAllIgnoreCase(name).isPresent()) {
            throw new CommonAppException(GENRE_EXISTS + genreRepository.findByNameAllIgnoreCase(name).get().getId());
        } else {
            Genre genre = new Genre();
            genre.setName(name);
            List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList);
            genre.setSubgenreList(subgenreList);
            return genreRepository.save(genre);
        }
    }

    @Transactional
    public Genre update(Integer id, String name, List<Integer> subgenreIdList) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new CommonAppException(NO_GENRE_MESSAGE + id));
        genre.setName(name);
        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList);
        genre.setSubgenreList(subgenreList);
        return genreRepository.save(genre);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!genreRepository.existsById(id)) {
            throw new CommonAppException(NO_GENRE_MESSAGE + id);
        } else genreRepository.deleteById(id);
    }
}
