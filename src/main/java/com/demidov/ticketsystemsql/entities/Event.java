package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private ZonedDateTime beginDateTime;

    @OneToOne
    private Venue venue;

    @OneToMany
    private List<Artist> artistList;

    @OneToOne
    private Genre genre;

    @OneToMany
    private List<Subgenre> subgenreList;

    @OrderBy("rowNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList;
}
