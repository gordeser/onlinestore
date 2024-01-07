package cz.cvut.fit.onlinestore.controller;

import cz.cvut.fit.onlinestore.dao.dto.UsersLoginDTO;
import cz.cvut.fit.onlinestore.dao.dto.UsersDescriptionDTO;
import cz.cvut.fit.onlinestore.dao.entity.Product;
import cz.cvut.fit.onlinestore.dao.entity.Users;
import cz.cvut.fit.onlinestore.service.UsersService;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailAlreadyExistsException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatEmailDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.UserWithThatIdDoesNotExistException;
import cz.cvut.fit.onlinestore.service.exceptions.WrongPasswordException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Access to users")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final UsersService usersService;

    @Operation(summary = "Log user in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "404", description = "Invalid credentials", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/api/users/login")
    public ResponseEntity<Users> getUserUsingCredentials(@RequestBody UsersLoginDTO userLogin) {
        try {
            Users user = usersService.authUser(userLogin);
            return ResponseEntity.ok(user);
        } catch (UserWithThatEmailDoesNotExistException | WrongPasswordException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "404", description = "User with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/api/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usersService.getUserById(id));
        } catch (UserWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Users.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/api/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            return ResponseEntity.ok(usersService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update user by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "400", description = "User with that email already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "User with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/api/users/{id}")
    public ResponseEntity<Users> updateUserById(@PathVariable Long id, @RequestBody UsersDescriptionDTO userUpdate) {
        try {
            return ResponseEntity.ok(usersService.updateUserById(id, userUpdate));
        } catch (UserWithThatEmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete user by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful operation", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "User with that id does not exist", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            usersService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (UserWithThatIdDoesNotExistException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Users.class))}),
            @ApiResponse(responseCode = "400", description = "User with that email already exists", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/api/users/signup")
    public ResponseEntity<Users> signupUser(@RequestBody UsersDescriptionDTO userSignup) {
        try {
            Users user = usersService.signupUser(userSignup);
            return ResponseEntity.ok(user);
        } catch (UserWithThatEmailAlreadyExistsException e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
