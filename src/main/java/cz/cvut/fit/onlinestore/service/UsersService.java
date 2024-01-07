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

    public Users getUserById(Long userId) {
        Optional<Users> user = usersRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserWithThatIdDoesNotExistException();
        }

        return user.get();
    }

    @Transactional
    public Users updateUserById(Long userId, UsersDescriptionDTO userUpdate) {
        Optional<Users> user = usersRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserWithThatIdDoesNotExistException();
        }

        Optional<Users> checkUser = usersRepository.findByEmail(userUpdate.email());
        // emails cannot be the same
        if (checkUser.isPresent() && !checkUser.get().getId().equals(userId) && checkUser.get().getEmail().equals(userUpdate.email())) {
            throw new UserWithThatEmailAlreadyExistsException();
        }

        Users updatedUser = user.get();
        updatedUser.setName(userUpdate.name());
        updatedUser.setSurname(userUpdate.surname());
        updatedUser.setAddress(userUpdate.address());
        updatedUser.setEmail(userUpdate.email());
        updatedUser.setPassword(userUpdate.password());

        return usersRepository.save(updatedUser);
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
