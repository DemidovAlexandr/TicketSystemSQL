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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Nullable
    @ToString.Exclude
    private List<Subgenre> subgenreList;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Nullable
    @ToString.Exclude
    private List<Event> eventList;
}
