package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Subgenre {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Genre genre;
}
