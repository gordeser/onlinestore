package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailAlreadyExistsException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public Users authUser(UsersLoginDTO userLogin) {
        Optional<Users> user = usersRepository.findByEmail(userLogin.email());

        if (user.isEmpty()) {
            throw new UserWithThatEmailDoesNotExistException();
        }

        if (!user.get().getPassword().equals(userLogin.password())) {
            throw new WrongPasswordException();
        }

        return user.get();
    }

    public Users signupUser(UsersDescriptionDTO userSignup) {
        if (usersRepository.findByEmail(userSignup.email()).isPresent()) {
            throw new UserWithThatEmailAlreadyExistsException();
        }

        Users newUser = new Users();
        newUser.setName(userSignup.name());
        newUser.setSurname(userSignup.surname());
        newUser.setAddress(userSignup.address());
        newUser.setEmail(userSignup.email());
        newUser.setPassword(userSignup.password());

        return usersRepository.save(newUser);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(Long id) {
        Optional<Users> user = usersRepository.findUsersById(id);

        if (user.isEmpty()) {
            throw new UserWithThatIdDoesNotExistException();
        }

        return user.get();
    }

    @Transactional
    public UsersDescriptionDTO updateUserById(Long id, UsersDescriptionDTO userUpdate) {
        int updatedCount = usersRepository.updateUser(
                id,
                userUpdate.name(),
                userUpdate.surname(),
                userUpdate.address(),
                userUpdate.email(),
                userUpdate.password()
        );

        if (updatedCount == 0) {
            throw new UserWithThatIdDoesNotExistException();
        }

        return userUpdate;
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
        } else {
            throw new UserWithThatIdDoesNotExistException();
        }
    }
}
