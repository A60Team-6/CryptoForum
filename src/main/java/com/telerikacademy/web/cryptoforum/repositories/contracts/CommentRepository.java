package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.FilteredCommentOptions;

import java.util.List;

public interface CommentRepository {

    Comment getCommentById(int id);

    void createComment(Comment comment);

    void removeComment(Comment comment);

    List<Comment> getAll(FilteredCommentOptions filteredCommentOptions);
}
