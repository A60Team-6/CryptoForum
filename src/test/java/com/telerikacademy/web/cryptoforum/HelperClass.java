package com.telerikacademy.web.cryptoforum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerikacademy.web.cryptoforum.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HelperClass {

    public static ObjectMapper objectMapper = new ObjectMapper();


    public static User createMockUserUser(){
        var mockUser = new User();

        mockUser.setId(1);
        mockUser.setFirstName("Peter");
        mockUser.setLastName("Jackson");
        mockUser.setUsername("Petejack");
        mockUser.setEmail("ppp.jjj@example.com");
        mockUser.setPassword("15975248653");
        mockUser.setPosition(new Position(3, "user"));
        mockUser.setBlocked(false);
        mockUser.setProfilePhoto(null);
        mockUser.setCreatedAt(LocalDateTime.now());

        return mockUser;
    }

    public static User createMockUserAdmin(){
        var mockUser = new User();

        mockUser.setId(2);
        mockUser.setFirstName("Gogo");
        mockUser.setLastName("Tomov");
        mockUser.setUsername("Gotogoto");
        mockUser.setEmail("ggg.ttt@example.com");
        mockUser.setPassword("15975253");
        mockUser.setPosition(new Position(1, "admin"));
        mockUser.setBlocked(false);
        mockUser.setProfilePhoto(null);
        mockUser.setCreatedAt(LocalDateTime.now());

        return mockUser;
    }


    public static User createMockUserModerator(){
        var mockUser = new User();

        mockUser.setId(3);
        mockUser.setFirstName("Tea");
        mockUser.setLastName("Tairovich");
        mockUser.setUsername("Teattt");
        mockUser.setEmail("ttt.ttt@example.com");
        mockUser.setPassword("15874258796345");
        mockUser.setPosition(new Position(2, "moderator"));
        mockUser.setBlocked(false);
        mockUser.setProfilePhoto(null);
        mockUser.setCreatedAt(LocalDateTime.now());

        return mockUser;
    }

    public static Post createPost(){
        var post = new Post();

        post.setId(1);
        post.setTitle("Bitcoin");
        post.setContent("Bitcoins will 63.000 dollars next year.");
        post.setLikes(0);
        post.setComments(new ArrayList<Comment>());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }

    public static Comment createComment(){
        var comment = new Comment();

        comment.setId(1);
        comment.setContent("This is a comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        return comment;
    }

    public static Tag createTag(){
        var tag = new Tag();

        tag.setId(1);
        tag.setName("Tag");

        return tag;
    }
}
