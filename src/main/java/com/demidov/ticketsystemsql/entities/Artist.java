package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Artist {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @ManyToMany
    private List<Subgenre> subgenreList;

}
