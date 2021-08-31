package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Optional<List<Purchase>> findAllByUserId(Integer userId);

}
