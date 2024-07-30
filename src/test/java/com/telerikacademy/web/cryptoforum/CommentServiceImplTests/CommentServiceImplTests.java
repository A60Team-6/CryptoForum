package com.telerikacademy.web.cryptoforum.CommentServiceImplTests;


import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.CommentServiceImpl;
import com.telerikacademy.web.cryptoforum.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    CommentRepository mockRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void get_Should_Return_User_WhenProperIdIsProvided(){
        Comment comment = HelperClass.createComment();

        Mockito.when(mockRepository.getCommentById(1)).thenReturn(comment);

        assertEquals(commentService.getCommentById(1), comment);
    }

    @Test
    public void create_Should_Throw_When_User_IsBlocked(){
        User user = HelperClass.createMockUserUser();

        user.setBlocked(true);

        Post post = HelperClass.createPost();

        Comment comment = HelperClass.createComment();

        post.setUser(user);

        comment.setPost(post);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> commentService.createComment(comment, post));
    }

    @Test
    public void create_Should_Pass_When_Is_Valid(){

        User user = HelperClass.createMockUserUser();
        user.setBlocked(false);

        Post post = HelperClass.createPost();
        post.setUser(user);

        Comment comment = HelperClass.createComment();

        comment.setPost(post);

        commentService.createComment(comment, post);


        Assertions.assertEquals(1, post.getComments().size());
        Mockito.verify(mockRepository, Mockito.times(1)).createComment(comment);
    }

    @Test
    public void delete_Should_Throw_When_User_IsNot_Moderator_User_Or_CommentCreator(){
        User postCreator = HelperClass.createMockUserUser();
        User user = HelperClass.createMockUserUser();
        user.setId(11);
        user.setUsername("Kiko");

        Post post = HelperClass.createPost();

        post.setUser(postCreator);

        Comment comment = HelperClass.createComment();

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> commentService.removeComment(comment, post, user));
    }

    @Test
    public void delete_Should_Pass_When_IsValid(){
        User user = HelperClass.createMockUserUser();

        user.setBlocked(false);

        Post post = HelperClass.createPost();

        Comment comment = HelperClass.createComment();

        post.setUser(user);
        comment.setPost(post);
        commentService.removeComment(comment, post, user);

        Mockito.verify(mockRepository, Mockito.times(1)).removeComment(comment);
    }

    @Test
    public void get_Should_Return_AllComments_WhenOptionsAreFulfilled(){

        Comment comment = HelperClass.createComment();

        FilteredCommentOptions mock = Mockito.mock(FilteredCommentOptions.class);

        Mockito.when(mockRepository.getAll(mock)).thenReturn(List.of(comment));

        assertEquals(1, commentService.getAll(mock).size());
    }

    @Test
    public void update_Should_Throw_When_User_IsBlocked(){

        User user = HelperClass.createMockUserUser();

        user.setBlocked(true);

        Post post = HelperClass.createPost();
        Comment comment = HelperClass.createComment();

        post.setUser(user);
        comment.setPost(post);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> commentService.updateComment(user, comment));
    }

    @Test
    public void update_Should_Throw_When_User_IsSameUser(){

        User user = HelperClass.createMockUserUser();

        User otherUser = HelperClass.createMockUserUser();
        otherUser.setId(15);
        otherUser.setUsername("PPPNJIKMK");

        Post post = HelperClass.createPost();

        Comment comment = HelperClass.createComment();

        post.setUser(user);
        comment.setPost(post);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> commentService.updateComment(otherUser, comment));
    }

    @Test
    public void update_Should_Pass_When_IsValid(){
        User user = HelperClass.createMockUserUser();

        user.setBlocked(false);

        Post post = HelperClass.createPost();
        Comment comment = HelperClass.createComment();
        comment.setUser(user);
        comment.setPost(post);
        commentService.updateComment(user, comment);

        Mockito.verify(mockRepository, Mockito.times(1)).updateComment(comment);
    }
}
