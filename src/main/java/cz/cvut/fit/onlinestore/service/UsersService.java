package cz.cvut.fit.onlinestore.service;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.dao.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;

    public Users authUser(UsersLoginDTO userLogin) {
        Optional<Users> user = repository.findUserByEmail(userLogin.email());

        if (user.isPresent() && user.get().getPassword().equals(userLogin.password())) {
            return user.get();
        }

        return null;
    }
}
