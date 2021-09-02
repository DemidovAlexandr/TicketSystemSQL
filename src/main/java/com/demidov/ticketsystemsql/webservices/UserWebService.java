package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.UserInDTO;
import com.demidov.ticketsystemsql.dto.out.UserOutDTO;
import com.demidov.ticketsystemsql.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWebService {

    private final UserService userService;

    public UserOutDTO getById(Integer id) {
        return userService.toOutDTO(userService.getById(id));
    }

    public List<UserOutDTO> getAll() {
        return userService.getAll()
                .stream()
                .map(userService::toOutDTO)
                .collect(Collectors.toList());
    }

    public UserOutDTO create(UserInDTO dto) {
        return userService.toOutDTO(userService.create(dto));
    }

    public UserOutDTO update(UserInDTO dto) {
        return userService.toOutDTO(userService.update(dto));
    }

    public void deleteById(Integer id){
        userService.deleteById(id);
    }
}
