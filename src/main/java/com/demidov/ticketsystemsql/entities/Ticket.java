package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer lineNumber;
    @Column(nullable = false)
    private Integer seatNumber;
    @Column(nullable = false)
    private Integer price;

    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Event event;


}
