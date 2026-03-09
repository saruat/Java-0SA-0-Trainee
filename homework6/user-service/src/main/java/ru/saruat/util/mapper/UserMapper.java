package ru.saruat.util.mapper;

import org.springframework.stereotype.Component;
import ru.saruat.dto.UserDTO;
import ru.saruat.entity.User;

@Component
public class UserMapper {
    public User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setAge(userDTO.age());
        return user;
    }

    public UserDTO convertToDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}
