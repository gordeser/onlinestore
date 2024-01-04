package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersSignupDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;

    public Users authUser(UsersLoginDTO userLogin) {
        Optional<Users> user = repository.findByEmail(userLogin.email());

        if (user.isPresent() && user.get().getPassword().equals(userLogin.password())) {
            return user.get();
        }

        return null;
    }

    @Modifying
    public boolean signupUser(UsersSignupDTO userSignup) {
        if (repository.findByEmail(userSignup.email()).isPresent()) {
            return false;
        }

        Users newUser = new Users();
        newUser.setName(userSignup.name());
        newUser.setSurname(userSignup.surname());
        newUser.setAddress(userSignup.address());
        newUser.setEmail(userSignup.email());
        newUser.setPassword(userSignup.password());

        repository.save(newUser);
        return true;
    }
}
