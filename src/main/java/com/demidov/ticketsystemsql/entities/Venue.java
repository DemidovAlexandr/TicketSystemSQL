package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Venue {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String city;
    private String streetAddress;
    private String description;
    private String contacts;
}
