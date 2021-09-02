package com.demidov.ticketsystemsql.dto.out;

import lombok.Data;

@Data
public class VenueOutDTO {

    private Integer id;
    private String name;
    private String city;
    private String streetAddress;
    private String description;
    private String contacts;
}
