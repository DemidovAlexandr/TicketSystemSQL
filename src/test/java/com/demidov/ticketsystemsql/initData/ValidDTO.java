package com.demidov.ticketsystemsql.initData;

import com.demidov.ticketsystemsql.dto.in.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Getter
@NoArgsConstructor
public class ValidDTO {

    private final UserInDTO deletedUserInDto = initDeletedUser();
    private final UserInDTO userInDTO = initUserDto();
    private final GenreInDTO genreInDTO = initGenreDTO();
    private final SubgenreInDTO subgenreInDTO = initSubgenreDTO();
    private final ArtistInDTO artistInDTO = initArtistDTO();
    private final VenueInDTO venueInDTO = initVenueDTO();
    private final EventInDTO eventInDTO = initEventDTO();
    private final TicketInDTO ticketInDTO = initTicketDTO();
    private final TicketInDTO availableTicket = initAvailableTicket();
    private final PurchaseInDTO purchaseInDTO = initPurchaseDTO();
    private final EventInDTO deletedEventDTO = initDeletedEventDTO();
    private final TicketInDTO deletedEventTicketDTO = initDeletedEventTicket();

    private UserInDTO initDeletedUser() {
        UserInDTO dto = new UserInDTO();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setDateOfBirth(LocalDate.of(1985, 11, 5));
        dto.setCity("Moscow");
        dto.setTelephone("+79231234567");
        dto.setEmail("john@johnwebsite.com");
        dto.setDeleted(true);
        return dto;
    }

    private UserInDTO initUserDto() {
        UserInDTO dto = new UserInDTO();
        dto.setName("Bob");
        dto.setSurname("Smith");
        dto.setDateOfBirth(LocalDate.of(1990, 10, 5));
        dto.setCity("Moscow");
        dto.setTelephone("+79991234567");
        dto.setEmail("bob@bobwebsite.bob");
        return dto;
    }

    private GenreInDTO initGenreDTO() {
        GenreInDTO dto = new GenreInDTO();
        dto.setName("Concert");
        return dto;
    }

    private SubgenreInDTO initSubgenreDTO() {
        SubgenreInDTO dto = new SubgenreInDTO();
        dto.setName("Metal");
        return dto;
    }

    private ArtistInDTO initArtistDTO() {
        ArtistInDTO dto = new ArtistInDTO();
        dto.setName("Slipknot");
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
        dto.setName("Rammstein concert");
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
        dto.setLineNumber(1);
        dto.setSeatNumber(1);
        return dto;
    }

    private TicketInDTO initAvailableTicket() {
        TicketInDTO dto = new TicketInDTO();
        dto.setEventId(1);
        dto.setPrice(8500);
        dto.setLineNumber(2);
        dto.setSeatNumber(1);
        return dto;
    }

    private PurchaseInDTO initPurchaseDTO() {
        PurchaseInDTO dto = new PurchaseInDTO();
        dto.setTicketIdList(List.of(1));
        dto.setUserId(1);
        dto.setPaidFor(false);
        return dto;
    }

    private EventInDTO initDeletedEventDTO() {
        EventInDTO dto = new EventInDTO();
        dto.setName("Deleted concert");
        dto.setBeginDate(LocalDate.of(2021, 12, 1));
        dto.setBeginTime(LocalTime.of(22,0));
        dto.setGenreId(1);
        dto.setSubgenreIdList(List.of(1));
        dto.setArtistIdList(List.of(1));
        dto.setTicketIdList(List.of(3));
        dto.setVenueId(1);
        dto.setDeleted(true);
        return dto;
    }

    private TicketInDTO initDeletedEventTicket() {
        TicketInDTO dto = new TicketInDTO();
        dto.setEventId(2);
        dto.setPrice(6000);
        dto.setLineNumber(1);
        dto.setSeatNumber(3);
        return dto;
    }
}
