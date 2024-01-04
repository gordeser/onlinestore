package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersSignupDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailIsAlreadySignedUpException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

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

    @Modifying
    public Users signupUser(UsersSignupDTO userSignup) {
        if (usersRepository.findByEmail(userSignup.email()).isPresent()) {
            throw new UserWithThatEmailIsAlreadySignedUpException();
        }

        Users newUser = new Users();
        newUser.setName(userSignup.name());
        newUser.setSurname(userSignup.surname());
        newUser.setAddress(userSignup.address());
        newUser.setEmail(userSignup.email());
        newUser.setPassword(userSignup.password());

        return usersRepository.save(newUser);
    }
}
