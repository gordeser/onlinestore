package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailAlreadyExistsException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UsersServiceUnitTest {
    @Autowired
    private UsersService usersService;

    @MockBean
    private UsersRepository usersRepository;

    private Users user;

    @Test
    public void authUserPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


        Users authed = usersService.authUser(new UsersLoginDTO(user.getEmail(), user.getPassword()));

        Assertions.assertEquals(user.getId(), authed.getId());
        Assertions.assertEquals(user.getName(), authed.getName());
        Assertions.assertEquals(user.getSurname(), authed.getSurname());
        Assertions.assertEquals(user.getAddress(), authed.getAddress());
        Assertions.assertEquals(user.getEmail(), authed.getEmail());
        Assertions.assertEquals(user.getPassword(), authed.getPassword());

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void authUserWrongEmail() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        String wrongEmail = "somewrongemail@email.com";
        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserWithThatEmailDoesNotExistException.class, () -> usersService.authUser(new UsersLoginDTO(wrongEmail, user.getPassword())));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
    }

    @Test
    public void authUserWrongPassword() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        String wrongPass = "somewrongpass";
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(WrongPasswordException.class, () -> usersService.authUser(new UsersLoginDTO(user.getEmail(), wrongPass)));

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void authUserWrongEmailPassword() {
        String wrongEmail = "somewrongemail@email.com";
        String wrongPass = "somewrongpass";
        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserWithThatEmailDoesNotExistException.class, () -> usersService.authUser(new UsersLoginDTO(wrongEmail, wrongPass)));
        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
    }

    @Test
    public void signUpUserPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(usersRepository.save(Mockito.any(Users.class))).thenReturn(user);

        Users authed = usersService.signupUser(descriptionDTO);

        Assertions.assertEquals(user.getName(), authed.getName());
        Assertions.assertEquals(user.getSurname(), authed.getSurname());
        Assertions.assertEquals(user.getAddress(), authed.getAddress());
        Assertions.assertEquals(user.getEmail(), authed.getEmail());
        Assertions.assertEquals(user.getPassword(), authed.getPassword());

        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(Users.class));
    }

    @Test
    public void signUpUserEmailExists() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        UsersDescriptionDTO descriptionDTO = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getEmail(),
                user.getPassword());

        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserWithThatEmailAlreadyExistsException.class, () -> usersService.signupUser(descriptionDTO));
        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void getAllUsersPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        Mockito.when(usersRepository.findAll()).thenReturn(List.of(user));

        List<Users> users = usersService.getAllUsers();
        Assertions.assertEquals(1, users.size());

        Users u = users.get(0);
        Assertions.assertEquals(user.getId(), u.getId());
        Assertions.assertEquals(user.getName(), u.getName());
        Assertions.assertEquals(user.getSurname(), u.getSurname());
        Assertions.assertEquals(user.getAddress(), u.getAddress());
        Assertions.assertEquals(user.getEmail(), u.getEmail());
        Assertions.assertEquals(user.getPassword(), u.getPassword());

        Mockito.verify(usersRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getUserByIdPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        Mockito.when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    @Test
    public void getUserByIdWrongId() {
        Long wrongId = -1L;
        Mockito.when(usersRepository.findById(wrongId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserWithThatIdDoesNotExistException.class, () -> usersService.getUserById(wrongId));
        Mockito.verify(usersRepository, Mockito.times(1)).findById(wrongId);
    }

    @Test
    public void updateUserByIdPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        String newAddress = "new address";

        Users newUser = new Users();
        newUser.setId(1L);
        newUser.setName("some name");
        newUser.setSurname("some surname");
        newUser.setAddress("new address");
        newUser.setEmail("email@example.com");
        newUser.setPassword("qwerty");

        Mockito.when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(usersRepository.save(Mockito.any(Users.class))).thenReturn(newUser);

        UsersDescriptionDTO userUpdate = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                newAddress,
                user.getEmail(),
                user.getPassword()
        );

        Users updatedUser = usersService.updateUserById(user.getId(), userUpdate);

        Assertions.assertEquals(user.getId(), updatedUser.getId());
        Assertions.assertEquals(user.getName(), updatedUser.getName());
        Assertions.assertEquals(user.getSurname(), updatedUser.getSurname());
        Assertions.assertEquals(newAddress, updatedUser.getAddress());
        Assertions.assertEquals(user.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(user.getPassword(), updatedUser.getPassword());

        Mockito.verify(usersRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(Users.class));
    }


    @Test
    public void updateUserByIdWrongId() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        String newAddress = "new address";
        Long wrongId = -1L;

        UsersDescriptionDTO userUpdate = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                newAddress,
                user.getEmail(),
                user.getPassword()
        );

        Mockito.when(usersRepository.findById(wrongId)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserWithThatIdDoesNotExistException.class, () -> usersService.updateUserById(wrongId, userUpdate));
        Mockito.verify(usersRepository, Mockito.times(1)).findById(wrongId);
    }

    @Test
    public void updateUserByIdEmailExists() {
        user = new Users();
        Users user2 = new Users();
        String wrongEmail = "exists@example.com";

        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");
        user2.setId(2L);
        user2.setEmail(wrongEmail);
        String newAddress = "new address";

        UsersDescriptionDTO userUpdate = new UsersDescriptionDTO(
                user.getName(),
                user.getSurname(),
                newAddress,
                wrongEmail,
                user.getPassword()
        );

        Mockito.when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(usersRepository.findByEmail(wrongEmail)).thenReturn(Optional.of(user2));

        Assertions.assertThrows(UserWithThatEmailAlreadyExistsException.class, () -> usersService.updateUserById(user.getId(), userUpdate));
        Mockito.verify(usersRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(usersRepository, Mockito.times(1)).findByEmail(wrongEmail);
    }

    @Test
    public void deleteUserByIdPositive() {
        user = new Users();
        user.setId(1L);
        user.setName("some name");
        user.setSurname("some surname");
        user.setAddress("some address");
        user.setEmail("email@example.com");
        user.setPassword("qwerty");

        Mockito.when(usersRepository.existsById(user.getId())).thenReturn(true);
        Mockito.doNothing().when(usersRepository).deleteById(user.getId());

        usersService.deleteUserById(user.getId());

        Mockito.verify(usersRepository, Mockito.times(1)).existsById(user.getId());
        Mockito.verify(usersRepository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    public void deleteUserByIdWrongId() {
        Long wrongId = -1L;
        Mockito.when(usersRepository.existsById(wrongId)).thenReturn(false);
        Mockito.doNothing().when(usersRepository).deleteById(wrongId);
        Assertions.assertThrows(UserWithThatIdDoesNotExistException.class, () -> usersService.deleteUserById(wrongId));

        Mockito.verify(usersRepository, Mockito.times(1)).existsById(wrongId);
        Mockito.verify(usersRepository, Mockito.times(0)).deleteById(wrongId);
    }
}
