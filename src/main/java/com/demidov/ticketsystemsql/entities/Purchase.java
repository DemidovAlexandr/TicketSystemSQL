package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime purchaseDate;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private Event event;

    @OrderBy("price")
    @OneToMany(mappedBy = "purchase")
    @ToString.Exclude
    private List<Ticket> ticketList;

    @Column(nullable = false)
    private Integer total;

    public void addTicket(Ticket ticket) {
        this.ticketList.add(ticket);
        ticket.setPurchase(this);
    }

    public void removeTicket(Ticket ticket) {
        this.ticketList.remove(ticket);
        ticket.setPurchase(null);
    }

}
