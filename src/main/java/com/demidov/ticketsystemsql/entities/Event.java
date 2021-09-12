package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    @Nullable
    private Venue venue;

    @ManyToOne
    @Nullable
    private Genre genre;

    @JoinTable(name = "EVENT_ARTIST",
            joinColumns = @JoinColumn(name = "EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artist> artistList = new ArrayList<>();

    @JoinTable(name = "EVENT_SUBGENRE",
            joinColumns = @JoinColumn(name = "EVENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUBGENRE_ID"))
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subgenre> subgenreList = new ArrayList<>();

    @Nullable
    @OrderBy("lineNumber, seatNumber ASC")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketList = new ArrayList<>();

    public void addTicket(Ticket ticket) {
        assert ticketList != null;
        ticketList.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        assert ticketList != null;
        ticketList.remove(ticket);
        ticket.setEvent(null);
    }

    public void addSubgenre(Subgenre subgenre) {
        assert subgenreList != null;
        subgenreList.add(subgenre);
    }

    public void removeSubgenre(Subgenre subgenre) {
        assert subgenreList != null;
        subgenreList.remove(subgenre);
    }
}
