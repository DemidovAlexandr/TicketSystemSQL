package com.demidov.ticketsystemsql.services;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.entities.User;
import com.demidov.ticketsystemsql.exceptions.CommonAppException;
import com.demidov.ticketsystemsql.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    private final EntityManager entityManager;

    public User getById(Integer id, boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", isDeleted);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            session.disableFilter("deletedUserFilter");
            return user.get();
        } else {
            session.disableFilter("deletedUserFilter");
            throw new CommonAppException(NO_USER_MESSAGE + id);
        }
    }

    public List<User> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<User> users = userRepository.findAll();
        session.disableFilter("deletedUserFilter");

        return users;
    }

    @Transactional
    public User create(UserInDTO dto) {
        User user = new User();
        setData(user, dto);
        if(userRepository.findUserByEmail(dto.getEmail()).isPresent()) {
            throw new CommonAppException("This email is already registered: " + dto.getEmail());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(UserInDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonAppException(NO_USER_MESSAGE + dto.getId()));
        setData(user, dto);
        return userRepository.save(user);
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

    private void setData(User user, UserInDTO dto) {
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setCity(dto.getCity());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setDeleted(dto.isDeleted());
    }
}
