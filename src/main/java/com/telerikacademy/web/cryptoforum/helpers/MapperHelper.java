package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.PostDto;
import com.telerikacademy.web.cryptoforum.models.dtos.RegistrationDto;
import com.telerikacademy.web.cryptoforum.models.dtos.UserDto;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PositionService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperHelper {

    private final PositionService positionService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public MapperHelper(PositionService positionService, UserService userService, UserRepository userRepository, PostRepository postRepository) {
        this.positionService = positionService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
//        user.setPhoneNumber(null);
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
        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }
}
