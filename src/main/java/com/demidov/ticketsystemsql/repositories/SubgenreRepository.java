package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Subgenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubgenreRepository extends JpaRepository<Subgenre, Integer> {
    Optional<Subgenre> findByNameAllIgnoreCase(String name);

    @Query("select s from Subgenre s where s.id in :idList")
    List<Subgenre> findAllById(@Param("idList") List<Integer> idList);
}
