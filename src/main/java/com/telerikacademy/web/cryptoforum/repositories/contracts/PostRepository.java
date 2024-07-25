package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;

import java.util.List;

public interface PostRepository {

    Post getPostById(int id);

    void createPost(Post post);

    void updatePost(Post postForUpdate);

    void deletePost(int id);

    List<Post> getAll(FilteredPostsOptions filteredPostsOptions);

    List<Post> getMostLikedPosts();

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentlyCreated();
}
