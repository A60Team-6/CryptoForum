package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Post> getAll(FilteredPostsOptions filteredPostsOptions) {
        return repository.getAll(filteredPostsOptions);
    }

    @Override
    public Post getPostById(int id) {
        return repository.getPostById(id);
    }

    @Override
    public void createPost(Post post) {
        PermissionHelper.isBlocked(post.getUser(), "You are blocked.");
        boolean duplicateExists = true;
        try {
            repository.getPostById(post.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "title", post.getTitle());
        }

        repository.createPost(post);
    }

    @Override
    public void updatePost(User user, Post postForUpdate) {
        PermissionHelper.isBlocked(user, "You are blocked.");
        PermissionHelper.isAdminOrSameUser(user, postForUpdate.getUser(), "This user is not admin nor owner!");

        repository.updatePost(postForUpdate);
    }

    @Override
    public void deletePost(User user, Post post) {
        PermissionHelper.isAdminOrSameUser(user, post.getUser(), "This user is not admin nor owner!");

        repository.deletePost(post.getId());
    }

    @Override
    public List<Post> getAllPostsOfUser(int userId) {
        return List.of();
    }

    @Override
    public void likePost(Post post, User user) {
        Set<User> userWhoLikedThePost = post.getUsersWhoLikedPost();

        if (userWhoLikedThePost.contains(user)) {
            throw new UnsupportedOperationException("You can not like a post twice!");
        }

        post.setLikes(post.getLikes() + 1);
        userWhoLikedThePost.add(user);

        repository.updatePost(post);
    }

    @Override
    public void removeLike(Post post, User user) {
        Set<User> userWhoLikedThePost = post.getUsersWhoLikedPost();

        if (!userWhoLikedThePost.contains(user)) {
            throw new UnsupportedOperationException("You can not remove like without like it before!");
        }

        post.setLikes(post.getLikes() - 1);
        userWhoLikedThePost.remove(user);

        repository.updatePost(post);
    }

    @Override
    public List<Post> getMostLikedPosts() {
        return repository.getMostLikedPosts();
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        return repository.getMostCommentedPosts();
    }

    @Override
    public List<Post> getMostRecentlyCreated() {
        return repository.getMostRecentlyCreated();
    }
}
