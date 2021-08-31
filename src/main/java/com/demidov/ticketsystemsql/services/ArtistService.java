package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.ArtistRepository;
import com.demidov.ticketsystemsql.repositories.SubgenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private static final String NO_ARTIST_MESSAGE = "There is no such artist with id: ";
    private static final String NO_SUBGENRE_MESSAGE = "There is no such subgenre with id: ";
    private final ArtistRepository artistRepository;
    private final SubgenreRepository subgenreRepository;

    public ArtistService(ArtistRepository artistRepository, SubgenreRepository subgenreRepository) {
        this.artistRepository = artistRepository;
        this.subgenreRepository = subgenreRepository;
    }

    public Artist getById(Integer artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            return artist.get();
        } else throw new CommonAppException(NO_ARTIST_MESSAGE + artistId);
    }

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public List<Artist> getAllBySubgenreList(List<Integer> subgenreIdList) {
        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList).orElseThrow(() -> new CommonAppException(NO_SUBGENRE_MESSAGE + subgenreIdList));
        Optional<List<Artist>> artistList = artistRepository.getAllBySubgenreListOrderByNameAsc(subgenreList);
        if (artistList.isPresent()) {
            return artistList.get();
        } else throw new CommonAppException("No artists found by this subgenre criteria: " + subgenreList);
    }

    @Transactional
    public Artist create(String name, List<Integer> subgenreIdList) {
        Artist artist = new Artist();
        artist.setName(name);
        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList).orElseThrow(() -> new CommonAppException(NO_SUBGENRE_MESSAGE + subgenreIdList));
        artist.setSubgenreList(subgenreList);
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist updateName(Integer artistId, String name) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new CommonAppException(NO_ARTIST_MESSAGE + artistId));
        artist.setName(name);
        return artistRepository.save(artist);
    }


    @Transactional
    public Artist updateSubgenres(Integer artistId, List<Integer> subgenreIdList) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new CommonAppException(NO_ARTIST_MESSAGE + artistId));
        List<Subgenre> subgenreList = subgenreRepository.findAllById(subgenreIdList).orElseThrow(() -> new CommonAppException(NO_SUBGENRE_MESSAGE + subgenreIdList));
        artist.setSubgenreList(subgenreList);
        return artistRepository.save(artist);
    }

    @Transactional
    public void deleteById(Integer artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw new CommonAppException(NO_ARTIST_MESSAGE + artistId);
        } else artistRepository.deleteById(artistId);
    }
}
