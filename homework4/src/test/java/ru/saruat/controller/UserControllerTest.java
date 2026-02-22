package ru.saruat.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.saruat.dto.UserDTO;
import ru.saruat.service.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper; // для сериализации dto в json

    @Test
    public void whenGetAllUsers_thenReturnUserList() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(
                uuid,
                "Ivan",
                "ivan@mail.ru",
                30,
                LocalDateTime.now()
        );

        when(userService.findAll()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("[0].name", is("Ivan")))
                .andExpect(jsonPath("[0].email", is("ivan@mail.ru")))
                .andExpect(jsonPath("[0].age", is(30)));

        verify(userService, times(1)).findAll();
    }

    @Test
    public void whenGetUserById_thenReturnUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(
                uuid,
                "Mary",
                "mary@mail.ru",
                25,
                LocalDateTime.now()
        );

        when(userService.findById(uuid)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mary")));

        verify(userService, times(1)).findById(uuid);
    }

    @Test
    public void whenUserNotFound_thenReturnNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(userService.findById(uuid)).thenReturn(null);

        mockMvc.perform(get("/api/users/{id}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateUser_thenReturnCreatedUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDTO inputUserDTO = UserDTO.of(
                "Alex",
                "alex@mail.ru",
                73
        );

        UserDTO savedUserDTO = new UserDTO(
                uuid,
                "Alex",
                "alex@mail.ru",
                73,
                LocalDateTime.now()
        );

        when(userService.create(any(UserDTO.class))).thenReturn(savedUserDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Alex")))
                .andExpect(jsonPath("$.age", is(73)));
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserDTO updatedUserDTO = new UserDTO(
                uuid,
                "Alex Updated",
                "alex.updated@mail.ru",
                34,
                LocalDateTime.now()
        );

        when(userService.update(eq(uuid),any(UserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/api/users/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alex Updated")))
                .andExpect(jsonPath("$.email", is("alex.updated@mail.ru")))
                .andExpect(jsonPath("$.age",is(34)));

        verify(userService, times(1)).update(eq(uuid),any(UserDTO.class));
    }

    @Test
    public void whenDeleteUser_thenReturnNoContent() throws Exception{
        UUID uuid = UUID.randomUUID();

        doNothing().when(userService).delete(uuid);

        mockMvc.perform(delete("/api/users/{id}", uuid))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(uuid);
    }


}
