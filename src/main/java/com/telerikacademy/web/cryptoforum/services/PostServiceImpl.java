package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<Post> getAll(FilteredPostsOptions options) {
        return repository.getAll(options);
    }

    @Override
    public Post getPostById(int id) {
        return repository.getPostById(id);
    }

    @Override
    public Post getPostByTitle(String title) {
        return repository.getPostByTitle(title);
    }

    @Override
    public List<Post> getAllPostsOfUser(User user) {
        return repository.getPostsWithThisUser(user);
    }

    @Override
    public void createPost(Post post, User user) {
        PermissionHelper.isBlocked(user, "You are blocked.");
        repository.createPost(post);
    }

    @Override
    public void updatePost(User user, Post postForUpdate) {
        PermissionHelper.isBlocked(user, "You are blocked.");
        PermissionHelper.isSameUser(user, postForUpdate.getUser(), "This are not post owner!");

        repository.updatePost(postForUpdate);
    }

    @Override
    public void deletePost(User user, Post post) {
        PermissionHelper.isAdminOrModeratorOrPostCreator(user, post.getUser(), "This user is not admin nor owner!");

        repository.deletePost(post.getId());
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
        List<Post> mostRecentlyCreated = repository.getMostRecentlyCreated();
        return mostRecentlyCreated;
    }
}
