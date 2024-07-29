package com.telerikacademy.web.cryptoforum.PostServiceImplTests;

import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.BlockedException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.services.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTests {


    @Mock
    PostRepository mockRepository;

    @Mock
    private PermissionHelper permissionHelper;

    @InjectMocks
    PostServiceImpl postService;


    @Test
    public void get_Should_Return_AllPosts_WhenOptionsAreFulfilled(){

        Post post = HelperClass.createPost();

        FilteredPostsOptions mock = Mockito.mock(FilteredPostsOptions.class);

        Mockito.when(mockRepository.getAll(mock)).thenReturn(List.of(post));

        assertEquals(1, postService.getAll(mock).size());
    }

    @Test
    public void get_Should_Return_Post_WhenProperIdIsProvided(){
        Post post = HelperClass.createPost();

        Mockito.when(mockRepository.getPostById(1)).thenReturn(post);

        assertEquals(postService.getPostById(1), post);
    }

    @Test
    public void get_Should_Return_Post_WhenProperTitleIsProvided(){
        Post post = HelperClass.createPost();

        Mockito.when(mockRepository.getPostByTitle("Bitcoin")).thenReturn(post);

        assertEquals(postService.getPostByTitle("Bitcoin"), post);
    }


    @Test
    public void create_Should_Throw_When_User_IsBlocked(){

        User user = HelperClass.createMockUserUser();

        user.setBlocked(true);

        Post post = HelperClass.createPost();

        post.setUser(user);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> postService.createPost(post));
    }

    @Test
    public void create_Should_Pass_When_Is_Valid(){

        User user = HelperClass.createMockUserUser();

        user.setBlocked(false);

        Post post = HelperClass.createPost();

        post.setUser(user);
        postService.createPost(post);

        Mockito.verify(mockRepository, Mockito.times(1)).createPost(post);
    }


    @Test
    public void update_Should_Throw_When_User_IsBlocked(){

        User user = HelperClass.createMockUserUser();

        user.setBlocked(true);

        Post post = HelperClass.createPost();

        post.setUser(user);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> postService.updatePost(user, post));
    }

    @Test
    public void update_Should_Throw_When_User_IsSameUser(){

        User user = HelperClass.createMockUserUser();

        User otherUser = HelperClass.createMockUserUser();
        otherUser.setId(15);
        otherUser.setUsername("PPPNJIKMK");


        Post post = HelperClass.createPost();

        post.setUser(user);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> postService.updatePost(otherUser, post));
    }

    @Test
    public void update_Should_Pass_When_IsValid(){
        User user = HelperClass.createMockUserUser();

        user.setBlocked(false);

        Post post = HelperClass.createPost();

        post.setUser(user);
        postService.updatePost(user, post);

        Mockito.verify(mockRepository, Mockito.times(1)).updatePost(post);
    }


}
