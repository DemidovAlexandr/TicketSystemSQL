package com.demidov.ticketsystemsql.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String surname;
    private Date dateOfBirth;
    private Integer age;
    private String telephone;
    private String email;
    private String city;
}
