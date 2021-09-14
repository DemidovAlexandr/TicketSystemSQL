package com.demidov.ticketsystemsql.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInDTO {
    private Integer id;
    private String name;
    private String surname;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    private String telephone;
    private String email;
    private String city;

    //private List<Integer> purchaseIdList;

    private boolean isDeleted;
}
