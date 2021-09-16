package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import com.demidov.ticketsystemsql.services.UserService;
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

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
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
        String uri = "/users/{id}";
        Integer id = dataInitializer.getUser().getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.mockMvc.perform(get(uri, id).param("isDeleted", String.valueOf(false)).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(validDTO.getUserInDTO().getName()))
                .andExpect(jsonPath("telephone").value(validDTO.getUserInDTO().getTelephone()))
                .andExpect(jsonPath("dateOfBirth")
                        .value(validDTO.getUserInDTO().getDateOfBirth().format(formatter)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDeletedUserById() throws Exception {
        String uri = "/users/{id}";
        Integer id = dataInitializer.getDeletedUser().getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        this.mockMvc.perform(get(uri, id).param("isDeleted", String.valueOf(true)).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(validDTO.getDeletedUserInDto().getName()))
                .andExpect(jsonPath("telephone").value(validDTO.getDeletedUserInDto().getTelephone()))
                .andExpect(jsonPath("dateOfBirth")
                        .value(validDTO.getDeletedUserInDto().getDateOfBirth().format(formatter)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllNotDeleted() throws Exception {
        String uri = "/users/all";
        this.mockMvc.perform(get(uri).param("isDeleted", String.valueOf(false)).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].name").value(validDTO.getUserInDTO().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllDeleted() throws Exception {
        String uri = "/users/all";
        this.mockMvc.perform(get(uri).param("isDeleted", String.valueOf(true)).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].name").value(validDTO.getDeletedUserInDto().getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateWithTestName() throws Exception {
        String uri = "/users/";
        UserInDTO dto = validDTO.getUserInDTO();
        dto.setName("Test");
        dto.setEmail("test@email.com");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("email").value(dto.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateWithEmailTaken() throws Exception {
        String uri = "/users/";
        UserInDTO dto = validDTO.getUserInDTO();
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("This email is already registered")));
    }

    @Test
    public void testUpdateWithTestName() throws Exception {
        String uri = "/users/";
        UserInDTO dto = userService.toInDTO(dataInitializer.getUser());
        dto.setName("Test");
        String content = objectMapper.writeValueAsString(dto);
        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteById() throws Exception {
        String uri = "/users/{id}";
        Integer id = dataInitializer.getUser().getId();
        assertTrue(userRepository.existsById(id), "There is no User to delete with this id");
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertTrue(userRepository.existsById(id), "User deleted completely");
        assertTrue(userRepository.getById(id).isDeleted(), "User's deleted flag is set to false");
    }
}
