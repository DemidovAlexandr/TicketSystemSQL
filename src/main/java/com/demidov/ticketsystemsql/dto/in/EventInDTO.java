package com.demidov.ticketsystemsql.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EventInDTO {

    private Integer id;
    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;

    private Integer venueId;

    private Integer genreId;

    private List<Integer> artistIdList;

    private List<Integer> subgenreIdList;

    private List<Integer> ticketIdList;
}
