package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.PurchaseInDTO;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.PurchaseRepository;
import com.demidov.ticketsystemsql.services.PurchaseService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class PurchaseControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    PurchaseRepository purchaseRepository;

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
    public void testGetPurchaseById() throws Exception {
        String uri = "/purchases/{id}";
        Integer id = dataInitializer.getPurchase().getId();

        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("userId").value(1))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreatePurchase() throws Exception {
        String uri = "/purchases/";
        PurchaseInDTO dto = validDTO.getPurchaseInDTO();
        List<Integer> ticketIdList = List.of(dataInitializer.getAvailableTicket().getId());
        dto.setTicketIdList(ticketIdList);
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("total").value(dataInitializer.getAvailableTicket().getPrice()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePurchase() throws Exception {
        String uri = "/purchases/";
        PurchaseInDTO dto = purchaseService.toInDTO(dataInitializer.getPurchase());
        List<Integer> ticketIdList = List.of(dataInitializer.getAvailableTicket().getId(), dataInitializer.getTicket().getId());
        dto.setTicketIdList(ticketIdList);
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("total").value(dataInitializer.getAvailableTicket().getPrice()
                        + dataInitializer.getTicket().getPrice()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        String uri = "/purchases/all";

        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllByUser() throws Exception {
        String uri = "/purchases/all";
        this.mockMvc.perform(get(uri).param("userId", validDTO.getPurchaseInDTO().getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].userId").value(1))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePurchaseById() throws Exception {
        String uri = "/purchases/{id}";
        Integer id = dataInitializer.getPurchase().getId();

        assertTrue(purchaseRepository.existsById(id), "There is no purchase to delete with id " + id);
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(purchaseRepository.existsById(id), "Purchase was not deleted");
    }
}
