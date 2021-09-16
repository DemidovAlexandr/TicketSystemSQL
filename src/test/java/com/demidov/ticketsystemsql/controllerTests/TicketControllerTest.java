package com.demidov.ticketsystemsql.controllerTests;

import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.initData.DataInitializer;
import com.demidov.ticketsystemsql.initData.ValidDTO;
import com.demidov.ticketsystemsql.repositories.TicketRepository;
import com.demidov.ticketsystemsql.services.TicketService;
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
public class TicketControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

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
    public void testGetTicketById() throws Exception {
        String uri = "/events/tickets/{id}";
        Integer id = dataInitializer.getTicket().getId();

        this.mockMvc.perform(get(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("price").value(validDTO.getTicketInDTO().getPrice()))
                .andExpect(jsonPath("eventId").value(dataInitializer.getEvent().getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllTickets() throws Exception {
        String uri = "/events/tickets/all";
        this.mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTicketsByEvent() throws Exception {
        String uri = "/events/tickets/all";
        Integer eventId = dataInitializer.getEvent().getId();
        Integer price1 = validDTO.getTicketInDTO().getPrice();
        Integer price2 = validDTO.getAvailableTicket().getPrice();

        this.mockMvc.perform(get(uri).param("eventId", eventId.toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("$.[0].price").value(price1))
                .andExpect(jsonPath("$.[1].price").value(price2))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateTicket() throws Exception {
        String uri = "/events/tickets";
        TicketInDTO dto = validDTO.getAvailableTicket();
        dto.setSeatNumber(dto.getSeatNumber() + 1);
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("seatNumber").value(dto.getSeatNumber()))
                .andExpect(jsonPath("eventId").value(dto.getEventId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateNonUniqueTicket() throws Exception {
        String uri = "/events/tickets";
        TicketInDTO dto = validDTO.getAvailableTicket();
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("This ticket is not unique")));
    }

    @Test
    public void updateTicket() throws Exception {
        String uri = "/events/tickets";
        TicketInDTO dto = validDTO.getTicketInDTO();
        Integer id = dataInitializer.getTicket().getId();
        dto.setId(id);
        dto.setSeatNumber(dto.getSeatNumber() + 1);
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(jsonPath("id").value(dto.getId()))
                .andExpect(jsonPath("seatNumber").value(dto.getSeatNumber()))
                .andExpect(jsonPath("eventId").value(dto.getEventId()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNonUniqueTicket() throws Exception {
        String uri = "/events/tickets";
        TicketInDTO dto = validDTO.getTicketInDTO();
        Integer id = dataInitializer.getTicket().getId();
        dto.setId(id);
        dto.setLineNumber(dto.getLineNumber() + 1);
        String content = objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[0]", Matchers.containsString("This ticket is not unique")));
    }

    @Test
    public void testDeleteTicketById() throws Exception {
        String uri = "/events/tickets/{id}";
        Integer id = dataInitializer.getTicket().getId();

        assertTrue(ticketRepository.existsById(id), "No ticket to delete with id" + id);
        this.mockMvc.perform(delete(uri, id).contentType(MediaType.APPLICATION_JSON))
                .andDo(document(uri.replace("/", "\\")))
                .andExpect(status().isOk());
        assertFalse(ticketRepository.existsById(id), "Ticket was not deleted");

    }
}
