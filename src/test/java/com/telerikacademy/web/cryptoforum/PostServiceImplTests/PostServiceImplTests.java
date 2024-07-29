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
import jakarta.persistence.PostPersist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void delete_Should_Throw_When_User_IsNot_Moderator_User_Or_PostCreator(){
        User postCreator = HelperClass.createMockUserUser();
        User user = HelperClass.createMockUserUser();
        user.setId(11);
        user.setUsername("Kiko");

        Post post = HelperClass.createPost();

        post.setUser(postCreator);

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> postService.deletePost(user, post));
    }

    @Test
    public void delete_Should_Pass_When_IsValid(){
        User user = HelperClass.createMockUserUser();

        user.setBlocked(false);

        Post post = HelperClass.createPost();

        post.setUser(user);
        postService.deletePost(user, post);

        Mockito.verify(mockRepository, Mockito.times(1)).deletePost(post.getId());
    }

    @Test
    public void likePost_Should_IncreaseLikes_When_UserLikesForTheFirstTime() {

        Post post = HelperClass.createPost();
        User user = HelperClass.createMockUserUser();
        post.setUser(user);
        post.setUsersWhoLikedPost(new HashSet<>());

        postService.likePost(post, user);

        assertEquals(1, post.getLikes());
        assertTrue(post.getUsersWhoLikedPost().contains(user));
        verify(mockRepository, times(1)).updatePost(post);
    }

    @Test
    public void likePost_Should_Throw_When_User_Already_Liked(){
        User user = HelperClass.createMockUserUser();
        Post post = HelperClass.createPost();
        post.setUsersWhoLikedPost(new HashSet<>());
        post.getUsersWhoLikedPost().add(user);
        post.setLikes(1);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> postService.likePost(post, user));
    }


    @Test
    public void dislikePost_Should_IncreaseLikes_When_UserLikesForTheFirstTime() {

        Post post = HelperClass.createPost();
        User user = HelperClass.createMockUserUser();
        post.setUser(user);
        post.setUsersWhoLikedPost(new HashSet<>());
        post.getUsersWhoLikedPost().add(user);
        post.setLikes(1);

        postService.removeLike(post, user);

        assertEquals(0, post.getLikes());
        assertFalse(post.getUsersWhoLikedPost().contains(user));
        verify(mockRepository, times(1)).updatePost(post);
    }

    @Test
    public void dislikePost_Should_Throw_When_User_Already_Liked(){
        User user = HelperClass.createMockUserUser();
        Post post = HelperClass.createPost();
        post.setUsersWhoLikedPost(new HashSet<>());


        Assertions.assertThrows(UnsupportedOperationException.class, () -> postService.removeLike(post, user));
    }

    @Test
    public void get_Should_Return_MostLikedPosts_WhenOptionsAreFulfilled(){

        Post post = HelperClass.createPost();

        Mockito.when(mockRepository.getMostLikedPosts()).thenReturn(List.of(post));

        assertEquals(1, postService.getMostLikedPosts().size());
    }

    @Test
    public void get_Should_Return_MostCommentedPosts_WhenOptionsAreFulfilled(){

        Post post = HelperClass.createPost();

        Mockito.when(mockRepository.getMostCommentedPosts()).thenReturn(List.of(post));

        assertEquals(1, postService.getMostCommentedPosts().size());
    }

    @Test
    public void get_Should_Return_MostRecentPosts_WhenOptionsAreFulfilled(){

        Post post = HelperClass.createPost();

        Mockito.when(mockRepository.getMostRecentlyCreated()).thenReturn(List.of(post));

        assertEquals(1, postService.getMostRecentlyCreated().size());
    }
}
