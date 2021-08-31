package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(name = "wwwwwwwww",
        columnNames = {"name", "beginDateTime", "venue_id"})})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    //todo Split date and time into LocalDate and LocalTime. Change query and service accordingly.

    @Column(columnDefinition = "DATETIME")
    //@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime beginDateTime;

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
    @OrderBy("rowNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList;
}
