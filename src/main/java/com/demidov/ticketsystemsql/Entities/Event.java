package com.demidov.ticketsystemsql.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private Timestamp beginDateTime;

    @OneToOne
    private Venue venue;

    @OneToMany
    private List<Artist> artistList;

    @OneToOne
    private Genre genre;

    @OneToMany
    private List<Subgenre> subgenreList;

    @OneToMany
    private List<Ticket> ticketList;
}
