package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.EventInDTO;
import com.demidov.ticketsystemsql.dto.in.PurchaseInDTO;
import com.demidov.ticketsystemsql.dto.out.EventOutDTO;
import com.demidov.ticketsystemsql.dto.out.PurchaseOutDTO;
import com.demidov.ticketsystemsql.entities.Event;
import com.demidov.ticketsystemsql.entities.Purchase;
import com.demidov.ticketsystemsql.entities.Ticket;
import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.EventRepository;
import com.demidov.ticketsystemsql.repositories.PurchaseRepository;
import com.demidov.ticketsystemsql.repositories.TicketRepository;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final static String NO_ORDER_FOUND = "No order found with id: ";
    private final static String DTO_IS_NULL = "DTO must not be null";

    private final PurchaseRepository purchaseRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public Purchase getById(Integer id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isPresent()) {
            return optionalPurchase.get();
        } else throw new CommonAppException(NO_ORDER_FOUND + id);
    }

    public List<Purchase> getAllByUser(Integer userId) {
        Optional<List<Purchase>> optionalPurchases = purchaseRepository.findAllByUserId(userId);
        if (optionalPurchases.isPresent()) {
            return optionalPurchases.get();
        } else throw new CommonAppException("No orders found for user id " + userId);
    }

    @Transactional
    public Purchase create(Integer userId, Integer eventId, List<Integer> ticketIdList) {
        Purchase purchase = new Purchase();
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonAppException("No user found with id: " + userId));
        purchase.setUser(user);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new CommonAppException("No event found with id: " + eventId));
        purchase.setEvent(event);

        List<Ticket> tickets = ticketRepository.findAllById(ticketIdList).orElseThrow(() -> new CommonAppException("No tickets found from the id list: " + ticketIdList));
        purchase.setTicketList(tickets);

        purchase.setPurchaseDate(LocalDateTime.now());

        Integer total = 0;
        for (Ticket ticket : tickets
        ) {
            total = total + ticket.getPrice();
        }
        purchase.setTotal(total);
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Purchase update(Integer id, Integer userId, Integer eventId, List<Integer> ticketIdList) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() -> new CommonAppException(NO_ORDER_FOUND + id));

        User user = userRepository.findById(userId).orElseThrow(() -> new CommonAppException("No user found with id: " + userId));
        purchase.setUser(user);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new CommonAppException("No event found with id: " + eventId));
        purchase.setEvent(event);

        List<Ticket> tickets = ticketRepository.findAllById(ticketIdList).orElseThrow(() -> new CommonAppException("No tickets found from the id list: " + ticketIdList));
        purchase.setTicketList(tickets);

        purchase.setPurchaseDate(LocalDateTime.now());

        Integer total = 0;
        for (Ticket ticket : tickets
        ) {
            total = total + ticket.getPrice();
        }
        purchase.setTotal(total);
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) {
            throw new CommonAppException(NO_ORDER_FOUND + id);
        } else purchaseRepository.deleteById(id);
    }

    public PurchaseInDTO toInDTO(Purchase purchase) {
        return Optional.ofNullable(purchase)
                .map(entity -> mapper.convertValue(entity, PurchaseInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public PurchaseOutDTO toOutDTO(Purchase purchase) {
        return Optional.ofNullable(purchase)
                .map(entity -> mapper.convertValue(entity, PurchaseOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }
}
