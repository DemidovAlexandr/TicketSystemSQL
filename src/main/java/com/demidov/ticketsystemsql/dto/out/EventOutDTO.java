package com.demidov.ticketsystemsql.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EventOutDTO {

    private Integer id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate beginDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime beginTime;

    private Integer venueId;

    private Integer genreId;

    private List<Integer> artistIdList;

    private List<Integer> subgenreIdList;

    private List<Integer> ticketIdList;

    private boolean isDeleted;
}
