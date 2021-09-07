package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.dto.in.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Getter
@NoArgsConstructor
public class ValidDTO {

    private final UserInDTO userInDTO = initUserDto();
    private final GenreInDTO genreInDTO = initGenreDTO();
    private final SubgenreInDTO subgenreInDTO = initSubgenreDTO();
    private final ArtistInDTO artistInDTO = initArtistDTO();
    private final VenueInDTO venueInDTO = initVenueDTO();
    private final EventInDTO eventInDTO = initEventDTO();
    private final TicketInDTO ticketInDTO = initTicketDTO();
    private final PurchaseInDTO purchaseInDTO = initPurchaseDTO();


    private UserInDTO initUserDto() {
        UserInDTO dto = new UserInDTO();
        dto.setName("Bob");
        dto.setSurname("Smith");
        dto.setDateOfBirth(LocalDate.of(1990, 10, 5));
        dto.setCity("Moscow");
        dto.setTelephone("+79991234567");
        dto.setEmail("bob@bobwebsite.bob");
        dto.setPurchaseIdList(List.of(1));
        return dto;
    }

    private GenreInDTO initGenreDTO() {
        GenreInDTO dto = new GenreInDTO();
        dto.setName("Concert");
        dto.setSubgenreIdList(List.of(1));
        return dto;
    }

    private SubgenreInDTO initSubgenreDTO() {
        SubgenreInDTO dto = new SubgenreInDTO();
        dto.setName("Metal");
        dto.setGenreId(1);
        return dto;
    }

    private ArtistInDTO initArtistDTO() {
        ArtistInDTO dto = new ArtistInDTO();
        dto.setName("Slipknot");
        dto.setSubgenreIdList(List.of(1));
        return dto;
    }

    private VenueInDTO initVenueDTO() {
        VenueInDTO dto = new VenueInDTO();
        dto.setName("Aurora Concert Hall");
        dto.setCity("Saint-Petersburg");
        dto.setDescription("Современный клуб и концертный зал, где выступают российские и зарубежные рок-артисты");
        dto.setStreetAddress("Пироговская наб., 5/2");
        dto.setContacts("Телефон: 8 (812) 907-19-17");
        return dto;
    }

    private EventInDTO initEventDTO() {
        EventInDTO dto = new EventInDTO();
        dto.setName("Slipknot concert");
        dto.setBeginDate(LocalDate.of(2021, 10, 30));
        dto.setBeginTime(LocalTime.of(19,0));
        dto.setGenreId(1);
        dto.setSubgenreIdList(List.of(1));
        dto.setArtistIdList(List.of(1));
        dto.setTicketIdList(List.of(1));
        dto.setVenueId(1);
        return dto;
    }

    private TicketInDTO initTicketDTO() {
        TicketInDTO dto = new TicketInDTO();
        dto.setEventId(1);
        dto.setPrice(5000);
        dto.setRowNumber(1);
        dto.setSeatNumber(1);
        return dto;
    }

    private PurchaseInDTO initPurchaseDTO() {
        PurchaseInDTO dto = new PurchaseInDTO();
        dto.setEventId(1);
        dto.setTicketIdList(List.of(1));
        dto.setUserId(1);
        return dto;
    }
}
