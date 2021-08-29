package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
