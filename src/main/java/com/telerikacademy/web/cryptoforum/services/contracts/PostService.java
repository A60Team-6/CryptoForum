package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    List<Post> getAll(FilteredPostsOptions filteredPostsOptions);

    List<Post> getAll(FilteredPostsOptions options, int page, int pageSize);

    int countFilteredPosts(FilteredPostsOptions options);

    Post getPostById(int id);

    Post getPostByTitle(String title);

    List<Post> getAllPostsOfUser(User user);

    void createPost(Post post, User user);

    void updatePost(User user, Post post);

    void deletePost(User user, Post post);

    void likePost(Post post, User user);

    void removeLike(Post post, User user);

    List<Post> getMostLikedPosts();

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentlyCreated();


}

