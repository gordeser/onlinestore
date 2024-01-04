package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersSignupDTO;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.UsersService;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/api/login")
    public ResponseEntity<?> getUserUsingCredentials(@RequestBody UsersLoginDTO userLogin) {
        try {
            Users user = usersService.authUser(userLogin);
            if (user != null) {
                return ResponseEntity.ok().body(user);
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Users> signupUser(@RequestBody UsersSignupDTO userSignup) {
        try {
            Users user = usersService.signupUser(userSignup);
            return ResponseEntity.ok(user);
        } catch (UserWithThatEmailDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
