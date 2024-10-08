package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.*;

import java.util.List;

public interface CommentService {

    Comment getCommentById(int id);

    void createComment(Comment comment, Post post);

    void removeComment(Comment comment, Post post, User user);

    List<Comment> getAll(FilteredCommentOptions filteredCommentOptions);

    void updateComment(User user, Comment comment);
}

