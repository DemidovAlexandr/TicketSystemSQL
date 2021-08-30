package com.demidov.ticketsystemsql.entities;

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
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    @Nullable
    @ToString.Exclude
    private List<Subgenre> subgenreList;
}
