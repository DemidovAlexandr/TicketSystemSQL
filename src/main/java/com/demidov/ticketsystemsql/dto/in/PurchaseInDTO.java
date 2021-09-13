package com.demidov.ticketsystemsql.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseInDTO {

    private Integer id;

    private Integer userId;
    //private Integer eventId;

    private List<Integer> ticketIdList;
    
    private boolean isPaidFor;
}
