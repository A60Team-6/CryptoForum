package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void createComment(Comment comment, Post post) {
        User user = post.getUser();
        PermissionHelper.isBlocked(user, "You are blocked.");
        post.getComments().add(comment);
        commentRepository.createComment(comment);
    }

    @Override
    public void removeComment(Comment comment, Post commentedPost, User user) {
        User postPublicator = commentedPost.getUser();
        PermissionHelper.isAdminOrSameUser(user, postPublicator, "You can not remove comment, because this is not your post and you are not admin!");
        commentedPost.getComments().remove(comment);
        commentRepository.removeComment(comment);
    }

    @Override
    public List<Comment> getAll(FilteredCommentOptions filteredCommentOptions) {
        return commentRepository.getAll(filteredCommentOptions);
    }
}
