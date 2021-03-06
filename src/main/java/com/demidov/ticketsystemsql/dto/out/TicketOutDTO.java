package com.demidov.ticketsystemsql.dto.out;

import lombok.Data;

@Data
public class TicketOutDTO {

    private Integer id;
    private Integer lineNumber;
    private Integer seatNumber;
    private Integer price;
    private Integer eventId;
    private Integer purchaseId;
}
