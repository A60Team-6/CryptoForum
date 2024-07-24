package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    Comment getCommentById(int id);

    void createComment(Comment comment, Post post);

    void removeComment(Comment comment, Post post, User user);
}

