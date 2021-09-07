package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "DATE")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;

    @OneToOne
    private Venue venue;

    @OneToMany
    @ToString.Exclude
    private List<Artist> artistList;

    @OneToOne
    private Genre genre;

    @OneToMany
    @ToString.Exclude
    private List<Subgenre> subgenreList;

    @ToString.Exclude
    @Nullable
    @OrderBy("lineNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList;
}
