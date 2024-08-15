package com.telerikacademy.web.cryptoforum.controllers.mvc;

import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.helpers.ModelMapper;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.FilterPostDto;
import com.telerikacademy.web.cryptoforum.models.dtos.FilterUserDto;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {


    private final PostService postService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final MapperHelper mapperHelper;
    private final CommentService commentService;


    @Autowired
    public AdminMvcController(PostService postService, ModelMapper modelMapper, UserService userService, AuthenticationHelper authenticationHelper, MapperHelper mapperHelper, CommentService commentService) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.mapperHelper = mapperHelper;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/users")
    public String showAllUsers(@ModelAttribute("filteredUserOptions") FilterUserDto filterUserDto, BindingResult bindingResult, Model model, HttpSession session) {
        FilteredUserOptions filteredUserOptions = new FilteredUserOptions(
                filterUserDto.getUsername(),
                filterUserDto.getEmail(),
                filterUserDto.getFirstName(),
                filterUserDto.getCreateBefore(),
                filterUserDto.getCreateAfter(),
                filterUserDto.getSortBy(),
                filterUserDto.getSortOrder());
        User currentUser = authenticationHelper.tryGetUser(session);
        List<User> users = userService.getAll(filteredUserOptions, currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", users);
        return "AdminView";
    }
}
