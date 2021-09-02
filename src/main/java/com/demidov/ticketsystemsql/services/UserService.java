package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.TicketInDTO;
import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.out.TicketOutDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.entities.Ticket;
import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final static String NO_USER_MESSAGE = "There is no such user with id: ";
    private final static String DTO_IS_NULL = "DTO must not be null";

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public User getById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else throw new CommonAppException(NO_USER_MESSAGE + id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User create(String name, String surname, LocalDate dateOfBirth, String telephone, String email, String city) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(dateOfBirth);
        user.setTelephone(telephone);
        user.setEmail(email);
        user.setCity(city);

        return userRepository.save(user);
    }

    @Transactional
    public User update(Integer id, String name, String surname, LocalDate dateOfBirth, String telephone, String email, String city) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setSurname(surname);
            user.setDateOfBirth(dateOfBirth);
            user.setTelephone(telephone);
            user.setCity(email);
            user.setCity(city);
            return userRepository.save(user);
        } else throw new CommonAppException(NO_USER_MESSAGE + id);
    }

    @Transactional
    public void deleteById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CommonAppException(NO_USER_MESSAGE + id);
        } else userRepository.deleteById(id);
    }

    public UserInDTO toInDTO(User user) {
        return Optional.ofNullable(user)
                .map(entity -> mapper.convertValue(entity, UserInDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }

    public UserOutDTO toOutDTO(User user) {
        return Optional.ofNullable(user)
                .map(entity -> mapper.convertValue(entity, UserOutDTO.class))
                .orElseThrow(() -> new CommonAppException(DTO_IS_NULL));
    }
}
