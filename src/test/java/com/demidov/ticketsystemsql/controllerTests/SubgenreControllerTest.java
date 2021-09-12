package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.SubgenreRepository;
import com.demidov.ticketsystemsql.services.SubgenreService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class SubgenreControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SubgenreService subgenreService;

    @Autowired
    SubgenreRepository subgenreRepository;

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
    public void testGetSubgenreById() throws Exception {
        String uri = "/genres/subgenres/{id}";
        Integer id = dataInitializer.getSubgenre().getId();
        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dataInitializer.getSubgenre().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllSubgenres() throws Exception {
        String uri = "/genres/subgenres/all";
        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void createSubgenreWithTestName() throws Exception {
        String uri = "/genres/subgenres/";
        SubgenreInDTO dto = validDTO.getSubgenreInDTO();
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void createSubgenre_NameExists() throws Exception {
        String uri = "/genres/subgenres/";
        SubgenreInDTO dto = validDTO.getSubgenreInDTO();
        String content = objectMapper.writeValueAsString(dto);

        Throwable exception = assertThrows(CommonAppException.class, () ->
        {
            try {
                this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                        .andDo(document(uri.replace("/", "\\")));
            } catch (NestedServletException e) {
                throw e.getCause();
            }
        });
        assertEquals(exception.getMessage(), "Subgenre with such name already exists, id: "
                + dataInitializer.getSubgenre().getId());
    }

    @Test
    public void updateSubgenreWithTestName() throws Exception {
        String uri = "/genres/subgenres/";
        SubgenreInDTO dto = subgenreService.toInDTO(dataInitializer.getSubgenre());
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteById() throws Exception {
        String uri = "/genres/subgenres/{id}";
        Integer id = dataInitializer.getSubgenre().getId();

        assertTrue(subgenreRepository.existsById(id), "There is no subgenre to delete with id " + id);
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(subgenreRepository.existsById(id), "Subgenre was not deleted");
    }
}
