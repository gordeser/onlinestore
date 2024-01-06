package cz.cvut.fit.onlinestore.dao.repository;

import cz.cvut.fit.onlinestore.dao.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void findByEmailPositive() {
        var u1 = new Users();
        u1.setId(1L);
        u1.setEmail("test@test.com");
        u1.setName("test");
        usersRepository.save(u1);

        Optional<Users> user = usersRepository.findByEmail("test@test.com");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(u1.getEmail(), user.get().getEmail());
        Assertions.assertEquals(u1.getId(), user.get().getId());
    }

    @Test
    public void findByEmailFalse() {
        var u1 = new Users();
        u1.setId(1L);
        u1.setEmail("test@test.com");
        u1.setName("test");
        usersRepository.save(u1);

        Optional<Users> user = usersRepository.findByEmail("somewrongtest@gmail.com");

        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void updateUserPositive() {
        var u1 = new Users();
        u1.setId(1L);
        u1.setName("test");
        u1.setSurname("test surname");
        u1.setAddress("test address");
        u1.setEmail("test@test.com");
        u1.setPassword("pass");
        usersRepository.save(u1);

        int updatedCount = usersRepository.updateUser(
                u1.getId(),
                u1.getName(),
                "NEW SURNAME",
                u1.getAddress(),
                u1.getEmail(),
                u1.getPassword());

        Assertions.assertEquals(1, updatedCount);
    }

    @Test
    public void updateUserFalse() {
        var u1 = new Users();
        u1.setId(1L);
        u1.setName("test");
        u1.setSurname("test surname");
        u1.setAddress("test address");
        u1.setEmail("test@test.com");
        u1.setPassword("pass");
        usersRepository.save(u1);

        int updatedCount = usersRepository.updateUser(
                -1L,
                u1.getName(),
                "NEW SURNAME",
                u1.getAddress(),
                u1.getEmail(),
                u1.getPassword());

        Assertions.assertEquals(0, updatedCount);
    }
}
