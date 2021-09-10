package com.demidov.ticketsystemsql.dto.out;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOutDTO {
    private Integer id;

    private LocalDateTime purchaseDate;

    private Integer userId;
  //  private Integer eventId;

    private List<Integer> ticketIdList;

    private Integer total;
}
