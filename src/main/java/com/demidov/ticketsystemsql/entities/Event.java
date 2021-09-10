package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {

    /*ManyToMany SubgenreList
    * ManyToOne Genre
    * ManyToOne Venue
    * ManyToMany ArtistList
    * */

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

    @ManyToOne
    private Venue venue;

    @ManyToOne
    private Genre genre;

    @JoinTable(name = "Event_Subgenre",
            joinColumns = @JoinColumn(name = "Event_id"),
            inverseJoinColumns = @JoinColumn(name = "Subgenre_id"))
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subgenre> subgenreList = new ArrayList<>();

    @JoinTable(name = "EVENT_ARTIST",
            joinColumns = @JoinColumn(name = "EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artist> artistList = new ArrayList<>();

//    @JoinColumn
//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL)
//    @Nullable
//    private Genre genre;


//    @ManyToMany(mappedBy = "eventList", cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private List<Subgenre> subgenreList;

    @Nullable
    @OrderBy("lineNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList=new ArrayList<>();
}
