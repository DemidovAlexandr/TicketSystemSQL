package com.demidov.ticketsystemsql.webservices;

import com.demidov.ticketsystemsql.dto.in.SubgenreInDTO;
import com.demidov.ticketsystemsql.dto.out.SubgenreOutDTO;
import com.demidov.ticketsystemsql.services.SubgenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubgenreWebService {

    private final SubgenreService subgenreService;

    public SubgenreOutDTO getById(Integer id) {
        return subgenreService.toOutDTO(subgenreService.getById(id));
    }

    public List<SubgenreOutDTO> getAll() {
        return subgenreService.getAll().stream()
                .map(subgenreService::toOutDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubgenreOutDTO create(SubgenreInDTO dto) {
        return subgenreService.toOutDTO(subgenreService.create(dto));
    }

    @Transactional
    public SubgenreOutDTO update(SubgenreInDTO dto) {
        return subgenreService.toOutDTO(subgenreService.update(dto));
    }

    @Transactional
    public void deleteById(Integer id) {
        subgenreService.deleteById(id);
    }
}
