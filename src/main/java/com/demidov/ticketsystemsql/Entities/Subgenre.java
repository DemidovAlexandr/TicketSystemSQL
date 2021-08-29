package com.demidov.ticketsystemsql.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Subgenre {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToOne
    private Genre genre;
}
