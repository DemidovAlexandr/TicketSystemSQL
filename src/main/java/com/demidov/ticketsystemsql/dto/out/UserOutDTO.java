package com.demidov.ticketsystemsql.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserOutDTO {
    private Integer id;
    private String name;
    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    private String telephone;
    private String email;
    private String city;

    private boolean isDeleted;
}
