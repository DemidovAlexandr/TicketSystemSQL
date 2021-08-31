package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(name = "Event is unique",
        columnNames = {"name", "event_date_time", "venue"})})
public class Event {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime beginDateTime;

    @OneToOne
    private Venue venue;

    @OneToMany
    private List<Artist> artistList;

    @OneToOne
    private Genre genre;

    @OneToMany
    private List<Subgenre> subgenreList;

    @Nullable
    @OrderBy("rowNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList;
}
