package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.PurchaseInDTO;
import com.demidov.ticketsystemsql.dto.out.PurchaseOutDTO;
import com.demidov.ticketsystemsql.entities.Purchase;
import com.demidov.ticketsystemsql.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseWebService {

    private final PurchaseService purchaseService;

    public PurchaseOutDTO getById(Integer id) {
        return purchaseService.toOutDTO(purchaseService.getById(id));
    }

    public List<PurchaseOutDTO> getAll() {
        return purchaseService.getAll()
                .stream()
                .map(purchaseService::toOutDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseOutDTO> getAllByUser(Integer userId) {
        return purchaseService.getAllByUser(userId)
                .stream()
                .map(purchaseService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseOutDTO create(PurchaseInDTO dto) {
        return purchaseService.toOutDTO(purchaseService.create(dto));
    }

    @Transactional
    public PurchaseOutDTO update(PurchaseInDTO dto) {
        return purchaseService.toOutDTO(purchaseService.update(dto));
    }

    @Transactional
    public void deleteById(Integer id) {
        purchaseService.deleteById(id);
    }
}
