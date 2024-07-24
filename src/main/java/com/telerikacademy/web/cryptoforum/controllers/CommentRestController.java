package com.telerikacademy.web.cryptoforum.controllers;

import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.CommentDto;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;

    @Autowired
    public CommentRestController(CommentService commentService, MapperHelper mapperHelper, AuthenticationHelper authenticationHelper, PostService postService) {
        this.commentService = commentService;
        this.mapperHelper = mapperHelper;
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return commentService.getCommentById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(commentDto.getPostId());
            Comment comment = mapperHelper.createCommentFromDto(commentDto, post, user);
            commentService.createComment(comment, post);
            return new ResponseEntity<>("Congratulations! Comment has been successfully created!", HttpStatus.CREATED);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id, @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = getCommentById(id, headers);
            Post commentedPost = comment.getPost();
            commentService.removeComment(comment, commentedPost, user);
            return new ResponseEntity<>("Congratulations! You successfully delete the comment!", HttpStatus.OK);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
