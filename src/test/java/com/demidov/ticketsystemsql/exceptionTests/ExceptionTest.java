package com.demidov.ticketsystemsql.exceptionTests;

import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ExceptionTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

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
    public void CommonAppException() throws Exception {
        String uri = "/events/{id}";
        this.mockMvc.perform(get(uri, "-1").contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("There is no such event")));
    }

    @Test
    public void MethodArgumentMismatch() throws Exception {
        String uri = "/events/{id}";
        this.mockMvc.perform(get(uri, "xxx").contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("should be of type")));
    }

    @Test
    public void MissingServletRequestParameterException() throws Exception {
        String uri = "/events/search";
        String fromDate = LocalDate.of(2021, 9, 30).toString();
        String toDate = LocalDate.of(2021, 11, 5).toString();
        this.mockMvc.perform(get(uri).param("fromDate", fromDate).param("toDate", toDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("parameter is missing")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void RequestMethodNotSupported() throws Exception {
        String uri = "/users/";

        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("method is not supported")));
    }

    @Test
    public void HttpMediaTypeNotSupported() throws Exception {
        String uri = "/users/";
        String content = "";

        this.mockMvc.perform(post(uri).contentType(MediaType.TEXT_PLAIN).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("media type is not supported")));
    }
}
