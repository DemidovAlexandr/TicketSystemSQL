package com.demidov.ticketsystemsql.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String surname;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;
    private String city;

    @ToString.Exclude
    @OneToMany
    private List<Purchase> purchaseList;
}
