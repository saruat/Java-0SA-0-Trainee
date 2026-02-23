package ru.saruat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.stereotype.Service;
import ru.saruat.dto.UserDTO;
import ru.saruat.dto.UserEvent;
import ru.saruat.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.saruat.repository.UserRepository;
import ru.saruat.util.mapper.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Autowired
    public UserServiceImpl (UserRepository userRepository, UserMapper userMapper, KafkaTemplate<String, Object> kafkaTemplate){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public UserDTO findById(UUID id) {
        try {
            return userRepository.findById(id)
                    .map(userMapper::convertToDTO)
                    .orElse(null);
        } catch (Exception ex){
            logger.error("Ошибка при чтении пользователя:{}", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        try {
            User user = userMapper.convertToEntity(userDTO);
            user.setCreatedAt(java.time.LocalDateTime.now());
            User savedUser = userRepository.save(user);

            //ToDo user or dto?
            // Отправляем событие в Kafka
            UserEvent event = new UserEvent("CREATE", savedUser.getEmail());
            kafkaTemplate.send("user-events", event);

            return userMapper.convertToDTO(savedUser);
        } catch (Exception ex){
            logger.error("Ошибка при создании пользователя:{}", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public UserDTO update(UUID id, UserDTO userDTO) {
        try {
            User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException());
            existingUser.setName(userDTO.name());
            existingUser.setEmail(userDTO.email());
            existingUser.setAge(userDTO.age());
            User updatedUser = userRepository.save(existingUser);
            return userMapper.convertToDTO(updatedUser);
        } catch (Exception ex){
            logger.error("Ошибка при изменении пользователя:{}", ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userRepository.delete(user);

            // Отправляем событие в Kafka
            UserEvent event = new UserEvent("DELETE", user.getEmail());
            kafkaTemplate.send("user-events", event);

        } catch (Exception ex){
            logger.error("Ошибка при удалении пользователя:{}", ex.getMessage(), ex);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        try {
            return userRepository.findAll().stream()
                    .map(userMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Ошибка при получении списка пользователей: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

}