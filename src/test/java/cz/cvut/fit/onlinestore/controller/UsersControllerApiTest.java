package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.UsersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.UsersService;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailAlreadyExistsException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(UsersController.class)
public class UsersControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersService usersService;

    @Test
    public void getUserUsingCredentials() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        UsersLoginDTO login = new UsersLoginDTO(user.getEmail(), user.getPassword());
        Mockito.when(usersService.authUser(login)).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                user.getId(), user.getName(), user.getSurname(), user.getAddress(), user.getEmail(), user.getPassword()
                        )));
    }

    @Test
    public void getUserUsingCredentialsWrongPassword() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        UsersLoginDTO login = new UsersLoginDTO(user.getEmail(), user.getPassword());
        Mockito.when(usersService.authUser(login)).thenThrow(new WrongPasswordException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserUsingCredentialsWrongEmail() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        UsersLoginDTO login = new UsersLoginDTO(user.getEmail(), user.getPassword());
        Mockito.when(usersService.authUser(login)).thenThrow(new UserWithThatEmailDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        Mockito.when(usersService.getUserById(user.getId())).thenReturn(user);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/users/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                user.getId(), user.getName(), user.getSurname(), user.getAddress(), user.getEmail(), user.getPassword()
                        )));
    }

    @Test
    public void getUserByIdWrongEmail() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        Mockito.when(usersService.getUserById(user.getId())).thenThrow(new UserWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/users/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void getAllUsers() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        Mockito.when(usersService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("[{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}]",
                                user.getId(), user.getName(), user.getSurname(), user.getAddress(), user.getEmail(), user.getPassword()
                        )));
    }

    @Test
    public void updateUserById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                "new surname",
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersService.updateUserById(user.getId(), descriptionDTO)).thenReturn(user);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                        descriptionDTO.name(), descriptionDTO.surname(), descriptionDTO.address(), descriptionDTO.email(), descriptionDTO.password()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                user.getId(), user.getName(), user.getSurname(), user.getAddress(), user.getEmail(), user.getPassword()
                        )));
    }

    @Test
    public void updateUserByIdWrongEmail() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                "new surname",
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersService.updateUserById(user.getId(), descriptionDTO)).thenThrow(new UserWithThatEmailAlreadyExistsException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                        descriptionDTO.name(), descriptionDTO.surname(), descriptionDTO.address(), descriptionDTO.email(), descriptionDTO.password()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateUserByIdWrongId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("new surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                "new surname",
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersService.updateUserById(user.getId(), descriptionDTO)).thenThrow(new UserWithThatIdDoesNotExistException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/users/{id}", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                        descriptionDTO.name(), descriptionDTO.surname(), descriptionDTO.address(), descriptionDTO.email(), descriptionDTO.password()
                                )))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteUserById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        Mockito.doNothing().when(usersService).deleteUserById(user.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/users/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteUserByIdWrongId() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");
        Mockito.doThrow(new UserWithThatIdDoesNotExistException()).when(usersService).deleteUserById(user.getId());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/users/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void signupUser() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersService.signupUser(descriptionDTO)).thenReturn(user);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                        descriptionDTO.name(), descriptionDTO.surname(), descriptionDTO.address(), descriptionDTO.email(), descriptionDTO.password()
                                )))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        String.format("{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                user.getId(), user.getName(), user.getSurname(), user.getAddress(), user.getEmail(), user.getPassword()
                        )));
    }

    @Test
    public void signupUserWrongEmail() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setName("name");
        user.setSurname("surname");
        user.setEmail("email@email.com");
        user.setAddress("address");
        user.setPassword("password");

        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersService.signupUser(descriptionDTO)).thenThrow(new UserWithThatEmailAlreadyExistsException());

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("{\"name\":\"%s\",\"surname\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                        descriptionDTO.name(), descriptionDTO.surname(), descriptionDTO.address(), descriptionDTO.email(), descriptionDTO.password()
                                )))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
