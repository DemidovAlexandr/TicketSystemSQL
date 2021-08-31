package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final static String NO_USER_MESSAGE = "There is no such user with id: ";
    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else throw new CommonAppException(NO_USER_MESSAGE + id);
    }

    @Transactional
    public User create(String name, String surname, String dateOfBirth, String telephone, String email, String city) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        try {
            user.setDateOfBirth(formatter.parse(dateOfBirth));
        } catch (ParseException exception) {
            log.error("Wrong date format: {}", exception.getMessage());
        }
        user.setTelephone(telephone);
        user.setCity(email);
        user.setCity(city);

        return userRepository.save(user);
    }

    @Transactional
    public User update(Integer id, String name, String surname, String dateOfBirth, String telephone, String email, String city) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setSurname(surname);
            try {
                user.setDateOfBirth(formatter.parse(dateOfBirth));
            } catch (ParseException exception) {
                log.error("Wrong date format: {}", exception.getMessage());
            }
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
}
