package com.demidov.ticketsystemsql.dto.in;

import lombok.Data;

@Data
public class VenueInDTO {

    private Integer id;
    private String name;
    private String city;
    private String streetAddress;
    private String description;
    private String contacts;
}
