package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface PostService {

    Post getPostById(int id);

    void createPost(Post post);

    List<Post> getAllPostsOfUser(int userId);

    void updatePost(User user, Post post);

    void deletePost(User user, Post post);
}

