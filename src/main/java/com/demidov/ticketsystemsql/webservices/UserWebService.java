package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWebService {

    private final UserService userService;

    public UserOutDTO getById(Integer id, boolean isDeleted) {
        return userService.toOutDTO(userService.getById(id, isDeleted));
    }

    public List<UserOutDTO> getAll(boolean isDeleted) {
        return userService.getAll(isDeleted)
                .stream()
                .map(userService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserOutDTO create(UserInDTO dto) {
        return userService.toOutDTO(userService.create(dto));
    }

    @Transactional
    public UserOutDTO update(UserInDTO dto) {
        return userService.toOutDTO(userService.update(dto));
    }

    @Transactional
    public void deleteById(Integer id) {
        userService.deleteById(id);
    }
}
