package com.demidov.ticketsystemsql.dto.in;

import com.demidov.ticketsystemsql.entities.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventInDTO {

    private Integer id;
    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime beginDateTime;

    private Integer venueId;

    private List<Integer> artistIdList;

    private Integer genreId;

    private List<Integer> subgenreIdList;

    private List<Integer> ticketIdList;
}
