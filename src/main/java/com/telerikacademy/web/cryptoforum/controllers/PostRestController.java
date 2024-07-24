package com.telerikacademy.web.cryptoforum.controllers;

import com.telerikacademy.web.cryptoforum.exceptions.AuthenticationException;
import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.BlockedException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.PostDto;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostRestController(PostService postService, MapperHelper mapperHelper, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.mapperHelper = mapperHelper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return postService.getPostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody PostDto postDto, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = mapperHelper.createPostFromDto(postDto, user);
            postService.createPost(post);

            return new ResponseEntity<>("Congratulations, your post has been successfully created.",HttpStatus.OK);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BlockedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@Valid @RequestBody PostDto postDto, @RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = mapperHelper.updatePostFromDto(postDto,id);
            postService.updatePost(user, post);
            return new ResponseEntity<>("Your post was successfully updated.",HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (BlockedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    public List<Post> getAllPost() {
        return null;
    }

}
