package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.Comment;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    Comment getCommentById(int id);
}

