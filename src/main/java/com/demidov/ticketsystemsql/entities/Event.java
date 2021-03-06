package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Event.class)
@SQLDelete(sql = "UPDATE event SET deleted = true WHERE id=?")
@FilterDef(name = "deletedEventFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedEventFilter", condition = "deleted = :isDeleted")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "DATE")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate beginDate;

    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
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

    private boolean deleted = Boolean.FALSE;

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

    public void addArtist(Artist artist) {
        assert artistList != null;
        artistList.add(artist);
    }

    public void removeArtist(Artist artist) {
        assert artistList != null;
        artistList.remove(artist);
    }
}
