package com.demidov.ticketsystemsql.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOutDTO {
    private Integer id;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime purchaseDate;

    private Integer userId;

    private List<Integer> ticketIdList;

    private Integer total;
}
