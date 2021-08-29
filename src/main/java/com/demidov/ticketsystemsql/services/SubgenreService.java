package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.Genre;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.GenreRepository;
import com.demidov.ticketsystemsql.repositories.SubgenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubgenreService {

    private static final String NO_SUBGENRE_MESSAGE = "There is no such subgenre with id: ";
    private static final String SUBGENRE_EXISTS = "Subgenre with such name already exists, id: ";
    private static final String NO_GENRE_MESSAGE = "There is no such genre with id: ";
    private final SubgenreRepository subgenreRepository;
    private final GenreRepository genreRepository;

    public SubgenreService(SubgenreRepository subgenreRepository, GenreRepository genreRepository) {
        this.subgenreRepository = subgenreRepository;
        this.genreRepository = genreRepository;
    }

    public Subgenre getById(Integer id) {
        Optional<Subgenre> subgenre = subgenreRepository.findById(id);
        if (subgenre.isPresent()) {
            return subgenre.get();
        } else throw new CommonAppException(NO_SUBGENRE_MESSAGE + id);
    }

    public List<Subgenre> getAll() {
        return subgenreRepository.findAll();
    }

    public Subgenre create(String name, Integer genreId) {
        if (subgenreRepository.findByNameAllIgnoreCase(name).isPresent()) {
            throw new CommonAppException(SUBGENRE_EXISTS + subgenreRepository.findByNameAllIgnoreCase(name).get().getId());
        } else {
            Subgenre subgenre = new Subgenre();
            subgenre.setName(name);
            if (genreRepository.existsById(genreId)) {
                subgenre.setGenre(genreRepository.getById(genreId));
            } else throw new CommonAppException(NO_GENRE_MESSAGE + genreId);
            return subgenre;
        }
    }

    public Subgenre update(Integer id, String name, Integer genreId) {
        Subgenre subgenre = subgenreRepository.findById(id).orElseThrow(() -> new CommonAppException(NO_SUBGENRE_MESSAGE + id));
        subgenre.setName(name);
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new CommonAppException(NO_GENRE_MESSAGE + genreId));
        subgenre.setGenre(genre);
        return subgenreRepository.save(subgenre);
    }

    public void deleteById(Integer id) {
        if (!subgenreRepository.existsById(id)) {
            throw new CommonAppException(NO_SUBGENRE_MESSAGE + id);
        } else subgenreRepository.deleteById(id);
    }
}
