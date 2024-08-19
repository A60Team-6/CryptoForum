package com.telerikacademy.web.cryptoforum.controllers.mvc;

import com.telerikacademy.web.cryptoforum.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.ModelMapper;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.LoginDto;
import com.telerikacademy.web.cryptoforum.models.dtos.RegisterDto;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {


    private static final int ADMIN_POSITION = 1;
    private static final int MODERATOR_POSITION = 2;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper, UserService userService, ModelMapper modelMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "Login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());
            session.setAttribute("isAdmin", user.getPosition().getId() == ADMIN_POSITION);
            session.setAttribute("isModerator", user.getPosition().getId() == MODERATOR_POSITION);
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "Login";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "Register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto, BindingResult bindingResult,
                                 Model model) {
        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password");
        }

        if (userService.existsByEmail(registerDto.getEmail())) {
            bindingResult.rejectValue("email", "email_error", "Email is already in use");
        }

        if(userService.existsByUsername(registerDto.getUsername())){
            bindingResult.rejectValue("username", "username_error", "Username is already in use");
        }

        if (bindingResult.hasErrors()) {
            return "Register";
        }

        try {
            User user = modelMapper.fromDto(registerDto);
            model.addAttribute("user", user);

            if (!registerDto.getProfilePhoto().isEmpty()) {
                String profilePhotoFilename = saveProfilePhoto(registerDto.getProfilePhoto());
                user.setProfilePhoto(profilePhotoFilename);
            } else {
                user.setProfilePhoto("/assets/img/bitAvatar1.png");
            }

            userService.createUser(user);
            return "redirect:/auth/login";
        } catch (DuplicateEntityException | IOException e) {
//            if (e.getMessage().contains("username")) {
                bindingResult.rejectValue("username", "username_error", e.getMessage());
//            } else {
//                bindingResult.rejectValue("email", "email_error", e.getMessage());
//            }
            return "Register";
        }

    }

    private String saveProfilePhoto(MultipartFile profilePhoto) throws IOException {
        String uploadDir = "/assets/img";
       // String filename = UUID.randomUUID() + "-" + profilePhoto.getOriginalFilename();
        String filename =  profilePhoto.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath;
        try (InputStream inputStream = profilePhoto.getInputStream()) {
            assert filename != null;
            filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save profile photo: " + filename, e);
        }

        return filePath.toString();
    }

}
