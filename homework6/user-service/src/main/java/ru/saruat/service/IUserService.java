package ru.saruat.service;

import ru.saruat.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserDTO findById(UUID id);
    UserDTO create(UserDTO userDTO);
    UserDTO update(UUID id, UserDTO userDTO);
    void delete(UUID id);
    List<UserDTO> findAll();
}