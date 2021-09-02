package com.demidov.ticketsystemsql.dto.out;

import lombok.Data;

import java.util.List;

@Data
public class GenreOutDTO {

    private Integer id;
    private String name;
    private List<Integer> subgenreIdList;
}
