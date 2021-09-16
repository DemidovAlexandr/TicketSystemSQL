package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.ArtistInDTO;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.ArtistRepository;
import com.demidov.ticketsystemsql.services.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class ArtistControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ArtistService artistService;

    @Autowired
    ArtistRepository artistRepository;

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
    public void testDeleteArtistById() throws Exception {
        String uri = "/artists/{id}";
        Integer id = dataInitializer.getArtist().getId();
        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dataInitializer.getArtist().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllArtists() throws Exception {
        String uri = "/artists/all";
        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void createArtistWithTestName() throws Exception {
        String uri = "/artists/";
        ArtistInDTO dto = validDTO.getArtistInDTO();
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void createArtist_NameExists() throws Exception {
        String uri = "/artists/";
        ArtistInDTO dto = validDTO.getArtistInDTO();
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("Artist with that name already exists")));

    }

    @Test
    public void updateArtistWithTestName() throws Exception {
        String uri = "/artists/";
        ArtistInDTO dto = artistService.toInDTO(dataInitializer.getArtist());
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteById() throws Exception {
        String uri = "/artists/{id}";
        Integer id = dataInitializer.getArtist().getId();

        assertTrue(artistRepository.existsById(id), "There is no artist to delete with id " + id);
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(artistRepository.existsById(id), "Artist was not deleted");
    }
}
