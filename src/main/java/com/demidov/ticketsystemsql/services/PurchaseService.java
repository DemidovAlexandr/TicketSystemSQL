package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.PurchaseInDTO;
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
import java.util.ArrayList;
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

    public List<Purchase> getAll() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getAllByUser(Integer userId) {
        List<Purchase> purchases = purchaseRepository.findAllByUserId(userId);
        if (!purchases.isEmpty()) {
            return purchases;
        } else throw new CommonAppException("No orders found for user id " + userId);
    }

    @Transactional
    public Purchase create(PurchaseInDTO dto) {
        Purchase purchase = new Purchase();
        setData(purchase, dto);
        purchase = purchaseRepository.save(purchase);
        addTickets(purchase, dto);
        return purchase;
    }

    @Transactional
    public Purchase update(PurchaseInDTO dto) {
        Purchase purchase = purchaseRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_ORDER_FOUND + dto.getId()));

        setData(purchase, dto);
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) {
            throw new CommonAppException(NO_ORDER_FOUND + id);
        } else {
            Purchase purchase = optionalPurchase.get();
            List<Ticket> tickets = purchase.getTicketList();
            if(!tickets.isEmpty()) {
                for (Ticket ticket : tickets
                ) {
                    ticket.setPurchase(null);
                    //purchase.removeTicket(ticket);
                }
            }
            purchaseRepository.deleteById(id);
        }
    }

    public PurchaseInDTO toInDTO(Purchase purchase) {
        if(purchase == null) {
            throw new CommonAppException(DTO_IS_NULL);
        }
        PurchaseInDTO dto = new PurchaseInDTO();
        dto.setId(purchase.getId());
        dto.setUserId(purchase.getUser().getId());
        List<Integer> ticketIdList = new ArrayList<>();
        for (Ticket ticket : purchase.getTicketList()
             ) {
            ticketIdList.add(ticket.getId());
        }
        dto.setTicketIdList(ticketIdList);
        dto.setPaidFor(purchase.isPaidFor());
        return dto;
    }

    public PurchaseOutDTO toOutDTO(Purchase purchase) {
        if (purchase == null) throw new CommonAppException(DTO_IS_NULL);

        PurchaseOutDTO dto = new PurchaseOutDTO();
        dto.setId(purchase.getId());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        dto.setUserId(purchase.getUser().getId());
        List<Integer> ticketIdList = new ArrayList<>();
        for (Ticket ticket : purchase.getTicketList()
        ) {
            ticketIdList.add(ticket.getId());
        }
        dto.setTicketIdList(ticketIdList);
        dto.setTotal(purchase.getTotal());
        dto.setPaidFor(purchase.isPaidFor());
        return dto;
    }

    private void setData(Purchase purchase, PurchaseInDTO dto) {
        purchase.setPaidFor(dto.isPaidFor());
        purchase.setPurchaseDate(LocalDateTime.now());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CommonAppException("No user found with id: " + dto.getUserId()));
        purchase.setUser(user);

//        Event event = eventRepository.findById(dto.getEventId())
//                .orElseThrow(() -> new CommonAppException("No event found with id: " + dto.getEventId()));
//        purchase.setEvent(event);
    }

    @Transactional
    public void addTickets(Purchase purchase, PurchaseInDTO dto) {
        List<Ticket> tickets = ticketRepository.findAllById(dto.getTicketIdList());
        if (tickets.isEmpty()) {
            throw new CommonAppException("No tickets found from the id list: " + dto.getTicketIdList());
        }

        for (Ticket ticket : tickets
        ) {
            purchase.addTicket(ticket);
        }

        Integer total = 0;
        for (Ticket ticket : tickets
        ) {
            total = total + ticket.getPrice();
        }
        purchase.setTotal(total);
    }
}
