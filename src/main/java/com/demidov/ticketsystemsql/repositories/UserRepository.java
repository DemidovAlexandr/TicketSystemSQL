package com.demidov.ticketsystemsql.repositories;

import com.demidov.ticketsystemsql.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
