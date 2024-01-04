package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersSignupDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.UsersService;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/api/login")
    public ResponseEntity<Users> getUserUsingCredentials(@RequestBody UsersLoginDTO userLogin) {
        try {
            Users user = usersService.authUser(userLogin);
            return ResponseEntity.ok(user);
        } catch (UserWithThatEmailDoesNotExistException | WrongPasswordException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Users> signupUser(@RequestBody UsersSignupDTO userSignup) {
        try {
            Users user = usersService.signupUser(userSignup);
            return ResponseEntity.ok(user);
        } catch (UserWithThatEmailDoesNotExistException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
