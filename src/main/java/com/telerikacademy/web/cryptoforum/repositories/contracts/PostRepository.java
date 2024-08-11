package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface PostRepository {

    Post getPostById(int id);

    Post getPostByTitle(String title);

    void createPost(Post post);

    void updatePost(Post postForUpdate);

    void deletePost(int id);

    List<Post> getAll(FilteredPostsOptions filteredPostsOptions);

    List<Post> getMostLikedPosts();

    List<Post> getMostCommentedPosts();

    List<Post> getMostRecentlyCreated();

    void addTagToPost(User user, Post post, Tag tag);

    void deleteTagFromPost(User user, Post post, Tag tag);

    List<Post> getPostsWithThisUser(User user);
}
