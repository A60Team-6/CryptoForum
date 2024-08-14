package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.models.dtos.*;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PositionService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperHelper {

    private final PositionService positionService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public MapperHelper(PositionService positionService, UserService userService, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.positionService = positionService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public User updateUserFromDto(UserDto userDto, int id) {
        User user = userRepository.getById(id);
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        return user;
    }

    public User createUserFromRegistrationDto(RegistrationDto registrationDto) {
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setUsername(registrationDto.getUsername());
        user.setPosition(positionService.getPositionById(3));
        user.setProfilePhoto(null);
        user.setBlocked(false);
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public Post createPostFromDto(PostDto postDto, User user) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setLikes(0);
        post.setDislikes(0);

        return post;
    }

    public Post updatePostFromDto(PostDto postDto, int id) {
        Post post = postRepository.getPostById(id);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());;

        return post;
    }

    public Comment createCommentFromDto(CommentDto commentDto, Post post, User user) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        return comment;
    }

    public Comment createCommentFromMvcDto(CommentMvcDto commentMvcDto, Post post, User user){
        Comment comment = new Comment();
        comment.setContent(commentMvcDto.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        return comment;
    }

    public Comment updatedCommentFromDto(CommentDto commentDto, int id) {
        Comment comment = commentRepository.getCommentById(id);
        comment.setContent(commentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        return comment;
    }

    public Tag createTagFromDto(TagDto tagDto){
        Tag tag = new Tag();
        tag.setName(tagDto.getTag());

        return tag;
    }

    public AdminPhone addPhoneFromDto(PhoneNumberDto phoneNumberDto) {
        AdminPhone adminPhone = new AdminPhone();
        adminPhone.setPhoneNumber(phoneNumberDto.getPhoneNumber());

        return adminPhone;
    }
}
