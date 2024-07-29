package com.telerikacademy.web.cryptoforum.controllers;

import com.telerikacademy.web.cryptoforum.exceptions.*;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.PostDto;
import com.telerikacademy.web.cryptoforum.models.dtos.TagDto;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;
    private final TagService tagService;


    @Autowired
    public PostRestController(PostService postService, MapperHelper mapperHelper, AuthenticationHelper authenticationHelper, TagService tagService) {
        this.postService = postService;
        this.mapperHelper = mapperHelper;
        this.authenticationHelper = authenticationHelper;
        this.tagService = tagService;

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

            return new ResponseEntity<>("Congratulations, your post has been successfully created.", HttpStatus.CREATED);
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
            Post post = mapperHelper.updatePostFromDto(postDto, id);
            postService.updatePost(user, post);
            return new ResponseEntity<>("Your post was successfully updated.", HttpStatus.OK);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
            postService.deletePost(user, post);
            return new ResponseEntity<>("You have successfully deleted the post.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public List<Post> getAllPosts(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String content,
                                  @RequestParam(required = false) Integer minLikes,
                                  @RequestParam(required = false) Integer maxLikes,
                                  @RequestParam(required = false) String createBefore,
                                  @RequestParam(required = false) String createAfter,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        FilteredPostsOptions filteredPostsOptions = new FilteredPostsOptions(title, content, minLikes, maxLikes, createBefore, createAfter, sortBy, sortOrder);
        return postService.getAll(filteredPostsOptions);
    }

    @PutMapping("/{id}/like")
    public void likePost(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
            postService.likePost(post, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/removeLike")
    public void removeLike(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
            postService.removeLike(post, user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/top10MostLikedPosts")
    public List<Post> getMostLikedPosts() {
        return postService.getMostLikedPosts();
    }

    @GetMapping("/top10CommentedPosts")
    public List<Post> getMostCommentedPosts() {
        return postService.getMostCommentedPosts();
    }

    @GetMapping("/top10MostRecentlyCreated")
    public List<Post> getMostRecentlyCreated() {
        return postService.getMostRecentlyCreated();
    }


    @Transactional
    @PutMapping("/addTag/{postId}")
    public ResponseEntity<String> addTagToPost(@PathVariable int postId, @Valid @RequestBody TagDto tagDto, @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(postId);
            Tag tag = mapperHelper.createTagFromDto(tagDto);
            tagService.addTagToPost(user, post, tag);
            return new ResponseEntity<>("Congratulations, your tag has been successfully added to the post.", HttpStatus.CREATED);
        }catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (BlockedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/deleteTag/{postId}/{tagId}")
    public ResponseEntity<String> removeTagFromPost(@PathVariable int postId,@PathVariable int tagId, @Valid @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = tagService.getById(tagId);
            Post post = tag.getPosts().stream().filter(p -> p.getId() == postId).findFirst().orElseThrow();
            tagService.removeTagFromPost(user, post, tag);
            return new ResponseEntity<>("Congratulations, your tag has been successfully removed from the post.", HttpStatus.OK);
        }catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (BlockedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}

