package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.GenreInDTO;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.GenreRepository;
import com.demidov.ticketsystemsql.services.GenreService;
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
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class GenreControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GenreService genreService;

    @Autowired
    GenreRepository genreRepository;

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
    public void testGetById() throws Exception {
        String uri = "/genres/{id}";
        this.mockMvc.perform(get(uri, dataInitializer.getGenre().getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dataInitializer.getGenre().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        String uri = "/genres/all";
        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateGenre_NameExists() throws Exception {
        String uri = "/genres/";
        GenreInDTO dto = validDTO.getGenreInDTO();
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("Genre with such name already exists")));


    }

    @Test
    public void testCreateGenreWithTestName() throws Exception {
        String uri = "/genres/";
        GenreInDTO dto = validDTO.getGenreInDTO();
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Test"));
    }

    @Test
    public void testUpdateGenreWithTestName() throws Exception {
        String uri = "/genres/";
        GenreInDTO dto = genreService.toInDTO(genreService.getByName(validDTO.getGenreInDTO().getName()));
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Test"));
    }

    @Test
    public void deleteGenreById() throws Exception {
        String uri = "/genres/{id}";
        Integer id = genreService.getByName(validDTO.getGenreInDTO().getName()).getId();
        assertTrue(genreRepository.existsById(id), "There is no genre to delete with this id");
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(genreRepository.existsById(id), "Genre was not deleted");
    }
}
