package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.initData.DataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class GenreControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    private boolean isInitialized;

    @BeforeAll
    public void dataSetUp() {
        if (!isInitialized) {
            dataInitializer.initData();
            isInitialized = true;
        }
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void testById() throws Exception {
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

}
