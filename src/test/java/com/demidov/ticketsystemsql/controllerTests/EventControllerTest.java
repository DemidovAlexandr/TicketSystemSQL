package com.demidov.ticketsystemsql.controllerTests;
import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.entities.Event;
import com.demidov.ticketsystemsql.entities.Subgenre;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.EventRepository;
import com.demidov.ticketsystemsql.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class EventControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    ValidDTO validDTO;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        dataInitializer.initData();
    }

    @Test
    public void testGetEventById() throws Exception {
        String uri = "/events/{id}";
        Integer id = dataInitializer.getEvent().getId();
        EventInDTO dto = eventService.toInDTO(dataInitializer.getEvent());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("beginDate").value(dto.getBeginDate().format(formatter)))
                .andExpect(jsonPath("beginTime").value(dto.getBeginTime().toString()))
                .andExpect(jsonPath("venueId").value(dto.getVenueId()))
                .andExpect(jsonPath("genreId").value(dto.getGenreId()))
                .andExpect(jsonPath("artistIdList").value(dto.getArtistIdList()))
                .andExpect(jsonPath("subgenreIdList").value(dto.getSubgenreIdList()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDeletedEventById() throws Exception {
        String uri = "/events/{id}";
        Integer id = dataInitializer.getDeletedEvent().getId();
        EventInDTO dto = validDTO.getDeletedEventDTO();

        this.mockMvc.perform(get(uri, id).param("isDeleted", String.valueOf(true)).contentType(MediaType.APPLICATION_JSON))
                        .andDo(document(uri.replace("/", "\\")))
                        .andExpect(jsonPath("name").value(dto.getName()))
                        .andExpect(status().isOk());

        assertTrue(eventService.getById(id, true).isDeleted());
    }

    @Test
    public void testGetAllEvents() throws Exception {
        String uri = "/events/all";

        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllEventsByVenue() throws Exception {
        String uri = "/events/venues/{id}";
        Integer id = dataInitializer.getVenue().getId();

        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(dataInitializer.getEvent().getName()));;
    }

    @Test
    public void getAllEventsByArtist() throws Exception {
        String uri = "/events/artists/{id}";
        Integer id = dataInitializer.getArtist().getId();

        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(dataInitializer.getEvent().getName()));
    }

    @Test
    public void testSearchByDatesAndCity() throws Exception {
        EventInDTO eventDto = validDTO.getEventInDTO();
        VenueInDTO venueDto = validDTO.getVenueInDTO();
        String uri = "/events/search";
        String fromDate = LocalDate.of(2021, 9, 30).toString();
        String toDate = LocalDate.of(2021, 11, 5).toString();
        String city = venueDto.getCity();

        this.mockMvc.perform(get(uri).param("fromDate", fromDate).param("toDate", toDate).param("city", city)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].name").value(eventDto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchByDatesAndCityAndGenre() throws Exception {
        EventInDTO eventDto = validDTO.getEventInDTO();
        VenueInDTO venueDto = validDTO.getVenueInDTO();
        String uri = "/events/search";
        String fromDate = LocalDate.of(2021, 9, 30).toString();
        String toDate = LocalDate.of(2021, 11, 5).toString();
        String city = venueDto.getCity();
        Integer genreId = eventDto.getGenreId();

        this.mockMvc.perform(get(uri).param("fromDate", fromDate)
                        .param("toDate", toDate)
                        .param("city", city)
                        .param("genreId", String.valueOf(genreId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].name").value(eventDto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchByDatesAndCityAndGenreAndSubgenre() throws Exception {
        EventInDTO eventDto = validDTO.getMovieEventInDTO();
        VenueInDTO venueDto = validDTO.getCinemaVenueDTO();

        String uri = "/events/search";
        String fromDate = LocalDate.of(2021, 9, 1).toString();
        String toDate = LocalDate.of(2021, 11, 5).toString();
        String city = venueDto.getCity();
        Integer genreId = eventDto.getGenreId();
        Integer subgenreId = dataInitializer.getComedySubgenre().getId();

        this.mockMvc.perform(get(uri).param("fromDate", fromDate)
                        .param("toDate", toDate)
                        .param("city", city)
                        .param("genreId", String.valueOf(genreId))
                        .param("subgenreId", String.valueOf(subgenreId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].name").value(eventDto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateEvent() throws Exception {
        EventInDTO dto = validDTO.getMovieEventInDTO();
        dto.setBeginDate(LocalDate.of(2021, 9, 26));

        String uri = "/events/";
        String content = objectMapper.writeValueAsString(dto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("beginDate").value(dto.getBeginDate().format(formatter)));
    }

    @Test
    public void testUpdateEvent() throws Exception {
        EventInDTO dto = eventService.toInDTO(dataInitializer.getMovieEvent());
        List<Integer> subgenreIdList = List.of(dataInitializer.getActionSubgenre().getId());

        dto.setBeginDate(dto.getBeginDate().plusDays(1L));
        dto.setBeginTime(dto.getBeginTime().plusHours(3L));
        dto.setSubgenreIdList(subgenreIdList);

        String uri = "/events/";
        String content = objectMapper.writeValueAsString(dto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("beginDate").value(dto.getBeginDate().format(formatter).toString()))
                .andExpect(jsonPath("beginTime").value(dto.getBeginTime().toString()))
                .andExpect(jsonPath("subgenreIdList").value(dto.getSubgenreIdList().get(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEventById() throws Exception {
        String uri = "/events/{id}";
        Integer id = dataInitializer.getEvent().getId();

        assertTrue(eventRepository.existsById(id), "No event to delete in repository");
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertTrue(eventRepository.existsById(id), "Event was deleted completely");
        assertTrue(eventRepository.getById(id).isDeleted(), "Event isDeleted flag is false");
    }
}
