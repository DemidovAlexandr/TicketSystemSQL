package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Purchase.class)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime purchaseDate;

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private User user;

    @OrderBy("price")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "purchase")
    private List<Ticket> ticketList = new ArrayList<>();

    @Column(nullable = false)
    private Integer total = 0;

    public void addTicket(Ticket ticket) {
        this.ticketList.add(ticket);
        ticket.setPurchase(this);
    }

    public void removeTicket(Ticket ticket) {
        this.ticketList.remove(ticket);
        ticket.setPurchase(null);
    }
}
