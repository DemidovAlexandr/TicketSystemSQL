package com.demidov.ticketsystemsql.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Subgenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;


    @ManyToOne
    private Genre genre;


    @JoinTable(name = "ARTIST_SUBGENRE",
            joinColumns = @JoinColumn(name = "SUBGENRE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    @ManyToMany
    @ToString.Exclude
    private List<Artist> artistList;

    @ToString.Exclude
    @JoinTable(name = "SUBGENRE_EVENT",
            joinColumns = @JoinColumn(name = "SUBGENRE_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    @ManyToMany
    private List<Event> eventList;
}
