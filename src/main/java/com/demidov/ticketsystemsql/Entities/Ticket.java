package com.demidov.ticketsystemsql.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer rowNumber;
    private Integer seatNumber;
    private Integer price;

}
