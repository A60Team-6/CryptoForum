package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Post;

public interface PostRepository {

    Post getPostById(int id);

}
