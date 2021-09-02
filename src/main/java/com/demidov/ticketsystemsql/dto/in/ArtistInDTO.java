package com.demidov.ticketsystemsql.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class ArtistInDTO {

    private Integer id;
    private String name;
    private List<Integer> subgenreIdList;
}
