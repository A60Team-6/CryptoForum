package com.telerikacademy.web.cryptoforum.controllers.mvc;

import com.telerikacademy.web.cryptoforum.exceptions.*;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.helpers.ModelMapper;
import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.models.dtos.*;
import com.telerikacademy.web.cryptoforum.services.contracts.AdminPhoneService;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {


    private final PostService postService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final MapperHelper mapperHelper;
    private final CommentService commentService;
    private final AdminPhoneService adminPhoneService;


    @Autowired
    public AdminMvcController(PostService postService, ModelMapper modelMapper, UserService userService, AuthenticationHelper authenticationHelper, MapperHelper mapperHelper, CommentService commentService, AdminPhoneService adminPhoneService) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.mapperHelper = mapperHelper;
        this.commentService = commentService;
        this.adminPhoneService = adminPhoneService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/users")
    public String showAllUsers(@ModelAttribute("filteredUserOptions") FilterUserDto filterUserDto,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int pageSize,
                               Model model, HttpSession session) {
        FilteredUserOptions filteredUserOptions = new FilteredUserOptions(
                filterUserDto.getUsername(),
                filterUserDto.getEmail(),
                filterUserDto.getFirstName(),
                filterUserDto.getCreateBefore(),
                filterUserDto.getCreateAfter(),
                filterUserDto.getSortBy(),
                filterUserDto.getSortOrder());
        User currentUser = authenticationHelper.tryGetUser(session);
        List<User> users = userService.getAll(filteredUserOptions, page, pageSize);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page + 1);
        int totalPages = (int) Math.ceil(userService.countFilteredUsers(filteredUserOptions) / (double) pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        return "UsersView";
    }

    @GetMapping("/users/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetUser(session);
            User user = userService.getById(admin, id);

            model.addAttribute("user", user);
            model.addAttribute("currentUser", admin);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e){
            return "AccessDeniedView";
        }
    }

    @GetMapping("/users/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        User user1;
        try {
           user1 = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            UserDto userDto = modelMapper.toDto(user1);
            model.addAttribute("user", user1);
            model.addAttribute("userDto", userDto);
            model.addAttribute("id", id);
            model.addAttribute("phone", new PhoneNumberDto());
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute("userDto") UserDto userDto,
                             BindingResult bindingResult, Model model, HttpSession session
    ) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("phone", new PhoneNumberDto());
            model.addAttribute("currentUser", user);
            model.addAttribute("user", userService.getById(user, id));
            return "UserUpdateView";
        }

        try {
            User user1 = modelMapper.fromDto(id, userDto);
            model.addAttribute("user", userService.getById(user, id));
            model.addAttribute("id", id);
            model.addAttribute("userToUpdate", user1);
            model.addAttribute("userToUpdatePosition", user1.getPosition().getId());
            model.addAttribute("phone", new PhoneNumberDto());


            userService.updateUser(user, user1);
            return "redirect:/admin/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "UserUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            String redirect;
            if(user.getUsername().equals(userService.getById(user, id).getUsername())){
                redirect = "redirect:/auth/logout";
            }else {
                redirect = "redirect:/admin/users";
            }
            userService.deleteUser(user, id);
            return redirect;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable int id, Model model, HttpSession session){

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            User user1 = userService.getById(user, id);
            model.addAttribute("currentUser", user);
            model.addAttribute("user", user1);
            model.addAttribute("id", id);
            userService.blockUser(user, id);
            return "redirect:/admin/users";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }catch (BlockedException e){
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }

    }


    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable int id, Model model, HttpSession session){

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            User user1 = userService.getById(user, id);
            model.addAttribute("currentUser", user);
            model.addAttribute("user", user1);
            model.addAttribute("id", id);
            userService.unblockUser(user, id);
            return "redirect:/admin/users";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }catch (BlockedException e){
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }

    }

    @PostMapping("/users/{id}/toModerator")
    public String userToModerator(@PathVariable int id, Model model, HttpSession session){

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToModerate = userService.getById(user, id);
            model.addAttribute("currentUser", user);
            model.addAttribute("user", userToModerate);
            model.addAttribute("id", id);
            userService.userToBeModerator(user, userToModerate);
            return "redirect:/admin/users";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    @PostMapping("/users/{id}/toUser")
    public String moderatorToUser(@PathVariable int id, Model model, HttpSession session){

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToModerate = userService.getById(user, id);
            model.addAttribute("currentUser", user);
            model.addAttribute("user", userToModerate);
            model.addAttribute("id", id);
            userService.userToBeNotModerator(user, userToModerate);
            return "redirect:/admin/users";
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/currentUser/Me")
    public String showCurrentUser( Model model, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetUser(session);
            User user = userService.getById(admin, admin.getId());
            String avatarUrl = (user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) ? user.getProfilePhoto() : "/assets/img/bitAvatar1.png";
            model.addAttribute("avatarUrl", avatarUrl);
            model.addAttribute("user", user);
            return "MeView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e){
            return "AccessDeniedView";
        }
    }

    @PostMapping("/addPhone/{id}")
    public String addPhoneNumberToAdmin(@PathVariable int id, @ModelAttribute("phone") PhoneNumberDto phoneNumberDto,BindingResult result, Model model, HttpSession session) {
        User admin;
        try {
            admin = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (result.hasErrors()) {
            UserDto userDto = modelMapper.toDto(admin);
            model.addAttribute("phone", new PhoneNumberDto());
            model.addAttribute("userDto", userDto);
            return "UserUpdateView";
        }

        model.addAttribute("user", userService.getById(admin, id));
        model.addAttribute("userId", id);
        model.addAttribute("phone", phoneNumberDto);
        UserDto userDto = null;
        try {
            User user = userService.getById(admin, admin.getId());
            userDto = modelMapper.toDto(user);
            AdminPhone adminPhone = modelMapper.fromPhoneDto(phoneNumberDto);
            adminPhoneService.addPhoneNumber(adminPhone, user);
            return "redirect:/admin/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            result.rejectValue("phoneNumber", "duplicate_post", e.getMessage());
            model.addAttribute("userDto", userDto);
            return "UserUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/phoneNumber/{id}/delete")
    public String deleteComment(@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            AdminPhone adminPhone = adminPhoneService.getPhoneNumberById(id, user);
            adminPhoneService.removePhoneFromAdmin(user, id);
            return "redirect:/admin/users/" + user.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
}
