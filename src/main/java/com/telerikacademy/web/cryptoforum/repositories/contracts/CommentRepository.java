package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Comment;

public interface CommentRepository {

    Comment getCommentById(int id);
}
