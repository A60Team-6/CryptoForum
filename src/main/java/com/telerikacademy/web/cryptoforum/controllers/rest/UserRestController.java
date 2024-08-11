package com.telerikacademy.web.cryptoforum.controllers.rest;

import com.telerikacademy.web.cryptoforum.exceptions.*;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.PhoneNumberDto;
import com.telerikacademy.web.cryptoforum.models.dtos.RegistrationDto;
import com.telerikacademy.web.cryptoforum.models.dtos.UserDto;
import com.telerikacademy.web.cryptoforum.services.contracts.AdminPhoneService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";

    private final UserService service;

    private final AuthenticationHelper authenticationHelper;

    private final AdminPhoneService phoneService;

    private final MapperHelper mapperHelper;

    @Autowired
    public UserRestController(UserService service, AuthenticationHelper authenticationHelper, AdminPhoneService phoneService, MapperHelper mapperHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.phoneService = phoneService;
        this.mapperHelper = mapperHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String createBefore,
                             @RequestParam(required = false) String createAfter,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortOrder) {
        try {
            FilteredUserOptions filterUserOptions = new FilteredUserOptions(username, email, firstName, createBefore, createAfter, sortBy, sortOrder);
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAll(filterUserOptions, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @GetMapping("/id/{id}")
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
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public User getByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            User userWhoWillBrowse = authenticationHelper.tryGetUser(headers);
            return service.getByEmail(email, userWhoWillBrowse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/firstName/{firstName}")
    public User getByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstName) {
        try {
            User userWhoWillBrowse = authenticationHelper.tryGetUser(headers);
            return service.getByFirstName(firstName, userWhoWillBrowse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody RegistrationDto registrationDto) {
        try {
            User user = mapperHelper.createUserFromRegistrationDto(registrationDto);
            service.createUser(user);
            return new ResponseEntity<>("Congratulations! User has been successfully created!", HttpStatus.CREATED);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userForUpdate = mapperHelper.updateUserFromDto(userDto, id);
            service.updateUser(user, userForUpdate);
            return userForUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (BlockedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{userId}/toModerator")
    public ResponseEntity<String> updateUserToModerator(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToModerator = service.getById(user, userId);
            service.userToBeModerator(user, userToModerator);
            return new ResponseEntity<>("Congratulations! This user is already a moderator!", HttpStatus.OK);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{userId}/fromModerator")
    public ResponseEntity<String> updateUserFromModerator(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userFromModerator = service.getById(user, userId);
            service.userToBeNotModerator(user, userFromModerator);
            return new ResponseEntity<>("Congratulations! This user is not more a moderator!", HttpStatus.OK);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.deleteUser(user, id);
            return new ResponseEntity<>("Congratulations! The user has been deleted successfully!", HttpStatus.OK);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.blockUser(user, id);
            return new ResponseEntity<>("This user was blocked successfully!", HttpStatus.OK);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.unblockUser(user, id);
            return new ResponseEntity<>("This user was unblocked successfully!", HttpStatus.OK);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/phone")
    public ResponseEntity<String> addPhoneNumber(@RequestHeader HttpHeaders headers,@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            AdminPhone adminPhone = mapperHelper.addPhoneFromDto(phoneNumberDto);
            phoneService.addPhoneNumber(adminPhone, user);
            return new ResponseEntity<>("Phone number added successfully.", HttpStatus.OK);
        }catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
