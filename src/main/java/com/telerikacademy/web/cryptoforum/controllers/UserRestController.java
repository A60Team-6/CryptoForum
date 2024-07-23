package com.telerikacademy.web.cryptoforum.controllers;

import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.RegistrationDto;
import com.telerikacademy.web.cryptoforum.models.dtos.UserDto;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {


    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";


    private final UserService service;
    private final AuthenticationHelper authenticationHelper;
    private final MapperHelper mapperHelper;


    @Autowired
    public UserRestController(UserService service, AuthenticationHelper authenticationHelper, MapperHelper mapperHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.mapperHelper = mapperHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAll(user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @GetMapping("id/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getById(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public User getByUsername(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User userWhoWillBrowse = authenticationHelper.tryGetUser(headers);
            return service.getUserByUsername(username, userWhoWillBrowse);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public User getByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email){
        try {
            User userWhoWillBrowse = authenticationHelper.tryGetUser(headers);
            return service.getByEmail(email, userWhoWillBrowse);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/firstName/{firstName}")
    public User getByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstName){
        try {
            User userWhoWillBrowse = authenticationHelper.tryGetUser(headers);
            return service.getByFirstName(firstName, userWhoWillBrowse);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody RegistrationDto registrationDto){
        try {
            User user = mapperHelper.createUserFromRegistrationDto(registrationDto);
            service.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public User updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userForUpdate = mapperHelper.updateUserFromDto(userDto, id);
            service.updateUser(user, userForUpdate);
            return userForUpdate;
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{userId}/toModerator")
    public ResponseEntity<String> updateUserToModerator(@RequestHeader HttpHeaders headers, @PathVariable int userId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToModerator = service.getById(user, userId);
            service.userToBeModerator(user, userToModerator);
            return new ResponseEntity<>("Congratulations this user is already a moderator!", HttpStatus.OK);
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }





    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.getPosition().getName().equals("admin") && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }
}
