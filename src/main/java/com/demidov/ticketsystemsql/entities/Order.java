package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Event event;

    @OrderBy("price")
    @OneToMany
    private List<Ticket> ticketList;

    @Column(nullable = false, precision = 0, scale = 2)
    private BigDecimal total;

}
