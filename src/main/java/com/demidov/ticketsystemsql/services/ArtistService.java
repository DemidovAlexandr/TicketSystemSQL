package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.ArtistInDTO;
import com.demidov.ticketsystemsql.dto.out.ArtistOutDTO;
import com.demidov.ticketsystemsql.entities.Artist;
import com.demidov.ticketsystemsql.entities.Event;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.ArtistRepository;
import com.demidov.ticketsystemsql.repositories.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private static final String NO_ARTIST_MESSAGE = "There is no such artist with id: ";
    private static final String DTO_IS_NULL = "DTO must not be null";

    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final ObjectMapper mapper;

    public Artist getById(Integer artistId) {
        Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isPresent()) {
            return artist.get();
        } else throw new CommonAppException(NO_ARTIST_MESSAGE + artistId);
    }

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    @Transactional
    public Artist create(ArtistInDTO dto) {
        if (artistRepository.findArtistByName(dto.getName()).isPresent()) {
            Artist artist = artistRepository.findArtistByName(dto.getName()).get();
            throw new CommonAppException("Artist with that name already exists, id: " + artist.getId());
        }
        Artist artist = new Artist();
        artist.setName(dto.getName());
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist update(ArtistInDTO dto) {
        Artist artist = artistRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_ARTIST_MESSAGE + dto.getId()));
        artist.setName(dto.getName());
        return artistRepository.save(artist);
    }

    @Transactional
    public void deleteById(Integer artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw new CommonAppException(NO_ARTIST_MESSAGE + artistId);
        } else {
            Artist artist = artistRepository.getById(artistId);
            List<Event> events = eventRepository.findAllByArtist(artist);
            if(!events.isEmpty()) {
                for (Event event : events
                     ) {
                    event.removeArtist(artist);
                }
            }
            artistRepository.deleteById(artistId);
        }
    }

    public ArtistInDTO toInDTO(Artist artist) {
        return Optional.ofNullable(artist)
                .map(entity -> mapper.convertValue(entity, ArtistInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public ArtistOutDTO toOutDTO(Artist artist) {
        return Optional.ofNullable(artist)
                .map(entity -> mapper.convertValue(entity, ArtistOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }
}
