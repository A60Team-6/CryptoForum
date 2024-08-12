//package com.telerikacademy.web.cryptoforum.controllers.mvc;
//
//import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
//import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
//import com.telerikacademy.web.cryptoforum.helpers.ModelMapper;
//import com.telerikacademy.web.cryptoforum.models.Comment;
//import com.telerikacademy.web.cryptoforum.models.Post;
//import com.telerikacademy.web.cryptoforum.models.User;
//import com.telerikacademy.web.cryptoforum.models.dtos.CommentDto;
//import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
//import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/comments")
//public class CommentMvcController {
//
//    private final CommentService commentService;
//    private final PostService postService;
//    private final ModelMapper modelMapper;
//    private final AuthenticationHelper authenticationHelper;
//
//    @Autowired
//    public CommentMvcController(CommentService commentService, PostService postService, ModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
//        this.commentService = commentService;
//        this.postService = postService;
//        this.modelMapper = modelMapper;
//        this.authenticationHelper = authenticationHelper;
//    }
//
//    @GetMapping("/new")
//    public String showNewCommentPage(Model model) {
//        model.addAttribute("comment", new CommentDto());
//
//        return "CommentCreateView";
//    }
//
//
//    @PostMapping("/new")
//    public String createComment(@Valid @ModelAttribute("comment") CommentDto commentDto,
//                                BindingResult bindingResult,
//                                Model model,
//                                HttpSession session) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//
//        if (bindingResult.hasErrors()) {
//            return "PostView";
//        }
//
//        try {
//            Comment comment = modelMapper.fromDto(commentDto);
//            commentService.createComment(comment, );
//        }
//
//        return "redirect:/posts";
//    }
//}
