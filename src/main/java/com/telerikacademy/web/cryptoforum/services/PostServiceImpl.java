package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public Post getPostById(int id) {
        return repository.getPostById(id);
    }

    @Override
    public List<Post> getAllPostsOfUser(int userId) {
        return List.of();
    }
}
