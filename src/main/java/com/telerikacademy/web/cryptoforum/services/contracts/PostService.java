package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface PostService {

    Post getPostById(int id);

    List<Post> getAllPostsOfUser(int userId);
}
