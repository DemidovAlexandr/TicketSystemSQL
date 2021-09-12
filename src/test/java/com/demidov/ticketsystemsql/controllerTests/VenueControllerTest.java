package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.VenueInDTO;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.VenueRepository;
import com.demidov.ticketsystemsql.services.VenueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class VenueControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VenueService venueService;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    ValidDTO validDTO;

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        dataInitializer.initData();
    }

    @Test
    public void testGetById() throws Exception {
        String uri = "/venues/{id}";
        this.mockMvc.perform(get(uri, dataInitializer.getVenue().getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dataInitializer.getVenue().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByNonExistingId() {
        String uri = "/venues/{id}";
        Integer id = -1;

        Throwable exception = assertThrows(CommonAppException.class, () -> {
            try {
                this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                        .andDo(document(uri.replace("/", "\\")));
            } catch (NestedServletException e) {
                throw e.getCause();
            }
        });
        assertEquals(exception.getMessage(), "There is no such venue with id: " + id);
    }

    @Test
    public void testGetAll() throws Exception {
        String uri = "/venues/all";
        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllByCity() throws Exception {
        String uri = "/venues/all";
        this.mockMvc.perform((get(uri).param("city", validDTO.getVenueInDTO().getCity())).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateVenueWithTestName() throws Exception {
        String uri = "/venues/";
        VenueInDTO dto = validDTO.getVenueInDTO();
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Test"));
    }

    @Test
    public void testUpdateVenueWithTestName() throws Exception {
        String uri = "/venues/";
        VenueInDTO dto = venueService.toInDTO(venueRepository.getById(dataInitializer.getVenue().getId()));
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Test"));
    }


    @Test
    public void deleteById() throws Exception {
        String uri = "/venues/{id}";
        Integer id = dataInitializer.getVenue().getId();
        assertTrue(venueRepository.existsById(id), "There is no venue to delete with this id");
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(venueRepository.existsById(id), "Venue was not deleted");
    }
}
