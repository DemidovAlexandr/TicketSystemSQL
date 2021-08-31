package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final static String NO_USER_MESSAGE = "There is no such user with id: ";
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
    public User create(String name, String surname, Date dateOfBirth, String telephone, String email, String city) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setDateOfBirth(dateOfBirth);
        user.setTelephone(telephone);
        user.setCity(email);
        user.setCity(city);

        return userRepository.save(user);
    }

    @Transactional
    public User update(Integer id, String name, String surname, Date dateOfBirth, String telephone, String email, String city) {
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
}
