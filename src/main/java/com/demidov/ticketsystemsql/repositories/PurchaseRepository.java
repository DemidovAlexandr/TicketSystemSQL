package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findAllByUserId(Integer userId);

}
