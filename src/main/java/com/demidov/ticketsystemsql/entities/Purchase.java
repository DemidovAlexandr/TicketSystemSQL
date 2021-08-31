package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue
    private Integer id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private ZonedDateTime purchaseDate;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Event event;

    @OrderBy("price")
    @OneToMany
    private List<Ticket> ticketList;

    @Column(nullable = false)
    private Integer total;

}
