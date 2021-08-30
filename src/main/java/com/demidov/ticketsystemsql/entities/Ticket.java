package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(name = "Ticket is unique",
        columnNames = {"row_number", "seat_number", "price", "event"})})
public class Ticket {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer rowNumber;
    @Column(nullable = false)
    private Integer seatNumber;
    @Column(nullable = false)
    private Integer price;

    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Event event;
}
