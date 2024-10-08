package com.telerikacademy.web.cryptoforum.controllers.mvc;

import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private static final int ADMIN = 1;
    private static final int MODERATOR = 2;

    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public HomeMvcController(AuthenticationHelper authenticationHelper, PostService postService, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.userService = userService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

//    @ModelAttribute("isBlocked")
//    public boolean populateIsBlocked(HttpSession session) {
//        User currentUser = (User) session.getAttribute("currentUser");
//        return currentUser != null && currentUser.isBlocked();
//    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("countOfUsers", userService.countUsers());
        model.addAttribute("countOfPosts", postService.countPosts());
        return "HomeView";
    }

    @GetMapping("/mostLikedPosts")
    public String showMostLikedPosts(Model model) {
        model.addAttribute("posts", postService.getMostLikedPosts());
        model.addAttribute("title", "Top 10 Most Liked Posts");
        return "PostListView";
    }

    @GetMapping("/mostCommentedPosts")
    public String showMostCommentedPosts(Model model) {
        model.addAttribute("posts", postService.getMostCommentedPosts());
        model.addAttribute("title", "Top 10 Most Commented Posts");
        return "PostListView";
    }

    @GetMapping("/mostRecentlyCreatedPosts")
    public String showMostRecentlyCreatedPosts(Model model) {
        model.addAttribute("posts", postService.getMostRecentlyCreated());
        model.addAttribute("title", "Top 10 Most Recently Created Posts");
        return "PostListView";
    }

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if (user.getPosition().getId() == ADMIN || user.getPosition().getId() == MODERATOR) {
                return "AdminView";
            }
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/post")
    public String showPostPage() {
        return "post";

    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }
}
