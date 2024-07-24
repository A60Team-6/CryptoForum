package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }

    @Override
    public void createComment(Comment comment, Post post){
        post.getComments().add(comment);
        commentRepository.createComment(comment);
    }

    @Override
    public void removeComment(Comment comment, Post commentedPost, User user){
        User postPublicator = commentedPost.getUser();
        PermissionHelper.isAdminOrSameUser(user, postPublicator, "You can not remove comment, because this is not your post and you are not admin!");
        commentedPost.getComments().remove(comment);
        commentRepository.removeComment(comment);
    }
}
