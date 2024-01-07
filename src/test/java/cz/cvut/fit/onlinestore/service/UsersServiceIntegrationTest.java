package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UsersServiceIntegrationTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    private Users user;

    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();

        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");

        usersRepository.save(user);
    }

    @Test
    public void authUser() {
        Users authed = usersService.authUser(new UsersLoginDTO(user.getEmail(), user.getPassword()));

        Assertions.assertEquals(user.getId(), authed.getId());
        Assertions.assertEquals(user.getName(), authed.getName());
        Assertions.assertEquals(user.getSurname(), authed.getSurname());
        Assertions.assertEquals(user.getAddress(), authed.getAddress());
        Assertions.assertEquals(user.getEmail(), authed.getEmail());
        Assertions.assertEquals(user.getPassword(), authed.getPassword());
    }


    public void signupUser() {
        // todo
    }

    @Test
    public void getAllUsers() {
        List<Users> allUsers = usersService.getAllUsers();

        Assertions.assertEquals(1, allUsers.size());

        Users testUser = allUsers.get(0);
        Assertions.assertEquals(user.getId(), testUser.getId());
        Assertions.assertEquals(user.getName(), testUser.getName());
        Assertions.assertEquals(user.getSurname(), testUser.getSurname());
        Assertions.assertEquals(user.getAddress(), testUser.getAddress());
        Assertions.assertEquals(user.getEmail(), testUser.getEmail());
        Assertions.assertEquals(user.getPassword(), testUser.getPassword());
    }

    @Test
    public void updateUserById() {
        Users updatedUser = usersService.updateUserById(user.getId(), new UsersDescriptionDTO(
                user.getName(),
                "new surname",
                user.getAddress(),
                user.getEmail(),
                user.getPassword()
        ));

        Assertions.assertEquals(user.getId(), updatedUser.getId());
        Assertions.assertEquals(user.getName(), updatedUser.getName());
        Assertions.assertEquals(user.getSurname(), updatedUser.getSurname());
        Assertions.assertEquals(user.getAddress(), updatedUser.getAddress());
        Assertions.assertEquals(user.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(user.getPassword(), updatedUser.getPassword());
    }

    public void updateUserByIdWrongId() {
        // todo
    }

    public void updateUserByIdExistsEmail() {
        // todo
    }
}
