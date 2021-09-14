package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    @ManyToOne(optional = false)
    private Event event;

    @JoinColumn
    @ManyToOne
    @Nullable
    private Purchase purchase;

    @Override
    public String toString() {
        return "Ticket [lineNumber=" + lineNumber + ", seatNumber=" + seatNumber + ", price=" + price + ", eventId=" + event.getId() + "]";
    }
}
