package com.demidov.ticketsystemsql.dto.in;

import lombok.Data;

@Data
public class TicketInDTO {

    private Integer id;
    private Integer rowNumber;
    private Integer seatNumber;
    private Integer price;
    private Integer eventId;

}
