package com.telerikacademy.web.cryptoforum.controllers.mvc;


import com.telerikacademy.web.cryptoforum.exceptions.*;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.helpers.ModelMapper;
import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.models.dtos.*;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.TagService;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostMvcController {


    private final PostService postService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final MapperHelper mapperHelper;
    private final CommentService commentService;
    private final TagService tagService;


    @Autowired
    public PostMvcController(PostService postService, ModelMapper modelMapper, UserService userService, AuthenticationHelper authenticationHelper, MapperHelper mapperHelper, CommentService commentService, TagService tagService) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.mapperHelper = mapperHelper;
        this.commentService = commentService;
        this.tagService = tagService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/free")
    public String showAllPostsBeforeAuth(@ModelAttribute("filteredPostsOptions") FilterPostDto filterPostDto,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "4") int pageSize,
                               Model model) {
        FilteredPostsOptions filteredPostsOptions = new FilteredPostsOptions(
                filterPostDto.getTitle(),
                filterPostDto.getContent(),
                filterPostDto.getMinLikes(),
                filterPostDto.getMaxLikes(),
                filterPostDto.getCreateBefore(),
                filterPostDto.getCreateAfter(),
                filterPostDto.getSortBy(),
                filterPostDto.getSortOrder());
        List<Post> posts = postService.getAll(filteredPostsOptions, page, pageSize);
        model.addAttribute("filteredPostsOptions", filterPostDto);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", (int) Math.ceil(postService.countFilteredPosts(filteredPostsOptions) / (double) pageSize));
        model.addAttribute("pageSize", pageSize);
        return "PostsViewFree";
    }


    @GetMapping
    public String showAllPosts(@ModelAttribute("filteredPostsOptions") FilterPostDto filterPostDto,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "4") int pageSize,
                               Model model, HttpSession session) {
        FilteredPostsOptions filteredPostsOptions = new FilteredPostsOptions(
                filterPostDto.getTitle(),
                filterPostDto.getContent(),
                filterPostDto.getMinLikes(),
                filterPostDto.getMaxLikes(),
                filterPostDto.getCreateBefore(),
                filterPostDto.getCreateAfter(),
                filterPostDto.getSortBy(),
                filterPostDto.getSortOrder());
        List<Post> posts = postService.getAll(filteredPostsOptions, page, pageSize);
        User currentUser = authenticationHelper.tryGetUser(session);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("filteredPostsOptions", filterPostDto);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", (int) Math.ceil(postService.countFilteredPosts(filteredPostsOptions) / (double) pageSize));
        model.addAttribute("pageSize", pageSize);
        return "PostsView";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        try {
            Post post = postService.getPostById(id);
            Set<Tag> tags = post.getTagsOfThePost();
            model.addAttribute("tags", tags);
            model.addAttribute("post", post);
            model.addAttribute("currentUser", authenticationHelper.tryGetUser(session));
            model.addAttribute("comment", new CommentDto());
            model.addAttribute("tag", new TagDto());
            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e){
            return "AccessDeniedView";
        }
    }

    @GetMapping("/postsOfUser")
    public String showUserPosts(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<Post> posts = postService.getAllPostsOfUser(user).stream().collect(Collectors.toList());
            model.addAttribute("posts", posts);
        } catch (AuthenticationFailureException e) {
            return "HomeView";
        }

        return "MyPostsView";
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        model.addAttribute("post", new PostDto());
        return "PostCreateView";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            return "PostCreateView";
        }

        try {
            Post post = mapperHelper.createPostFromDto(postDto, user);
            postService.createPost(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "PostCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditPostPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            Post post = postService.getPostById(id);
            PostDto postDto = modelMapper.toDto(post);
            model.addAttribute("postId", id);
            model.addAttribute("post", postDto);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult bindingResult, Model model, HttpSession session
    ) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            return "PostUpdateView";
        }

        try {
            Post post = modelMapper.fromDto(id, postDto);
            postService.updatePost(user, post);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "PostUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            postService.deletePost(user, postService.getPostById(id));
            return "HomeView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable int id, HttpSession session, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            Post post = postService.getPostById(id);
            postService.likePost(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (UnsupportedOperationException е) {
            model.addAttribute("error", "You can not like this post twice!");
            return "redirect:/posts";
        }
    }

    @PostMapping("/{id}/removeLike")
    public String removeLike(@PathVariable int id, HttpSession session, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            Post post = postService.getPostById(id);
            postService.removeLike(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (UnsupportedOperationException е) {
            model.addAttribute("error", "You can not like this post twice!");
            return "redirect:/posts";
        }
    }


    @PostMapping("/{id}")
    public String createComment(@PathVariable int id, @Valid @ModelAttribute("comment") CommentMvcDto commentDto,
                                BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        Post post;
        try {
            post = postService.getPostById(id);
            model.addAttribute("currentUser", user);
            model.addAttribute("post", post);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

        if (bindingResult.hasErrors()) {
            return "PostView";
        }

        try {
            Comment comment = mapperHelper.createCommentFromMvcDto(commentDto, post, user);
            commentService.createComment(comment, post);
            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/comment/{commentId}")
    public String showSingleComment(@PathVariable int id, @PathVariable int commentId, Model model, HttpSession session) {
        try {
            Post post = postService.getPostById(id);
            Comment comment = commentService.getCommentById(commentId);
            model.addAttribute("post", post);
            model.addAttribute("comment", comment);
            model.addAttribute("currentUser", authenticationHelper.tryGetUser(session));
            return "CommentView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/comment/{commentId}/update")
    public String showEditCommentPage(@PathVariable int id, @PathVariable int commentId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            Post post = postService.getPostById(id);
            Comment comment = commentService.getCommentById(commentId);
            CommentDto commentDto = modelMapper.toDto(comment);
            model.addAttribute("currentUser", user);
            model.addAttribute("post", post);
            model.addAttribute("commentId", comment.getId());
            model.addAttribute("comment", commentDto);
            return "CommentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/comment/{commentId}/update")
    public String updateComment(@PathVariable int id, @PathVariable int commentId, @Valid @ModelAttribute("comment") CommentDto commentDto,
                             BindingResult bindingResult, Model model, HttpSession session
    ) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/Login";
        }

        if (bindingResult.hasErrors()) {
            return "CommentUpdateView";
        }

        try {
            Post post = postService.getPostById(id);
            Comment comment = modelMapper.fromDto(commentId, commentDto);
            model.addAttribute("currentUser", user);
            model.addAttribute("id", id);
            model.addAttribute("post", post);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", comment);
            commentService.updateComment(user, comment);
            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "CommentUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable int id, @PathVariable int commentId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Post post = postService.getPostById(id);
            Comment comment = commentService.getCommentById(commentId);
            commentService.removeComment(comment, post, user);
            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("{postId}/tags/{id}")
    public String showSingleTag(@PathVariable int postId, @PathVariable int id, Model model, HttpSession session) {
        try {
            Tag tag = tagService.getById(id);
            Post post = postService.getPostById(postId);
            model.addAttribute("post", post);
            model.addAttribute("currentUser", authenticationHelper.tryGetUser(session));
            model.addAttribute("tag", tag);
            return "TagView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthenticationFailureException e){
            return "AccessDeniedView";
        }
    }


    @PostMapping("/{id}/newTag")
    public String createTag(@PathVariable int id, @Valid @ModelAttribute("tag") TagDto tagDto,
                                BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        Post post;
        try {
            post = postService.getPostById(id);
            model.addAttribute("currentUser", user);
            model.addAttribute("post", post);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

        if (bindingResult.hasErrors()) {
            return "PostView";
        }

        try {
            Tag tag = mapperHelper.createTagFromDto(tagDto);
            model.addAttribute("tagId", tag.getId());
            model.addAttribute("tag", tag);
            tagService.addTagToPost(user, post, tag);
            return "redirect:/posts/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @PostMapping("/{id}/tag/{tagId}/delete")
    public String deleteTag(@PathVariable int id, @PathVariable int tagId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Post post = postService.getPostById(id);
            Tag tag = tagService.getById(tagId);
            tagService.removeTagFromPost( user, post, tag);
            return "redirect:/posts/" + id;
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