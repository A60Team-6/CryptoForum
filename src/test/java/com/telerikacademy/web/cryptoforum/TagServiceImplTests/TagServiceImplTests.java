package com.telerikacademy.web.cryptoforum.TagServiceImplTests;


import com.telerikacademy.web.cryptoforum.HelperClass;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.TagRepository;
import com.telerikacademy.web.cryptoforum.services.CommentServiceImpl;
import com.telerikacademy.web.cryptoforum.services.TagServiceImpl;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTests {


    @Mock
    TagRepository mockRepository;

    @Mock
    PostRepository mockPostRepository;

    @Mock
    PostService mockPostService;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    public void get_Should_Return_User_WhenProperIdIsProvided(){

        Tag tag = HelperClass.createTag();

        when(mockRepository.getTagById(1)).thenReturn(tag);

        assertEquals(tagService.getById(1), tag);
    }



    @Test
    public void get_Should_Return_Tag_WhenProperNameIsProvided(){
        Tag tag = HelperClass.createTag();

        when(mockRepository.getTagByTagName("Tag")).thenReturn(tag);

        assertEquals(tagService.getTagByTagName("Tag"), tag);
    }

    @Test
    public void create_Should_ThrowException_When_TagWithSameNameExist() {
        Tag tag = HelperClass.createTag();

        when(mockRepository.getTagByTagName(tag.getName())).thenReturn(tag);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> tagService.createNewTag(tag));
    }

    @Test
    public void create_Should_Pass_When_Tag_NotFound(){
        Tag tag = HelperClass.createTag();

        when(mockRepository.getTagByTagName(tag.getName())).thenThrow(EntityNotFoundException.class);

        tagService.createNewTag(tag);

        verify(mockRepository, Mockito.times(1)).createTag(tag);
    }


    @Test
    public void delete_Should_Pass_When_IsValid(){
        User user = HelperClass.createMockUserAdmin();

        user.setBlocked(false);

        Post post = HelperClass.createPost();

        Tag tag = HelperClass.createTag();


        tag.setPosts(new HashSet<>());
        tag.getPosts().add(post);

        tagService.deleteTag(tag, user);

        Mockito.verify(mockRepository, Mockito.times(1)).deleteTag(tag);
    }


    @Test
    public void update_Should_Throw_When_User_IsSameUser(){

        User user = HelperClass.createMockUserUser();

        User otherUser = HelperClass.createMockUserUser();
        otherUser.setId(15);
        otherUser.setUsername("PPPNJIKMK");

        Post post = HelperClass.createPost();
        post.setUser(user);

        Tag tag = HelperClass.createTag();

        Assertions.assertThrows(UnauthorizedOperationException.class, () -> tagService.addTagToPost(otherUser, post, tag));
    }



    @Test
    public void addTagToPost_Should_AddNewTag_When_TagDoesNotExist() {

         User user = HelperClass.createMockUserUser();

         Post post = HelperClass.createPost();
         post.setUser(user);

         Tag tag = HelperClass.createTag();
         tag.setPosts(new HashSet<>());
         tag.getPosts().add(post);

         post.setTagsOfThePost(new HashSet<>());

         when(mockRepository.getTagByTagName(anyString()))
                .thenThrow(EntityNotFoundException.class)
                 .thenThrow(EntityNotFoundException.class)
                .thenReturn(tag);

        tagService.addTagToPost(user, post, tag);

        assertTrue(post.getTagsOfThePost().contains(tag));
        verify(mockRepository, times(1)).createTag(tag);
        verify(mockPostRepository, times(1)).updatePost(post);
    }


    @Test
    public void addTagToPost_Should_Throw_When_TagExist() {

        User user = HelperClass.createMockUserUser();

        Post post = HelperClass.createPost();
        post.setUser(user);

        Tag tag = HelperClass.createTag();
        tag.setPosts(new HashSet<>());
        tag.getPosts().add(post);

        post.setTagsOfThePost(new HashSet<>());
        post.getTagsOfThePost().add(tag);

        when(mockRepository.getTagByTagName(anyString()))
                .thenThrow(EntityNotFoundException.class)
                .thenThrow(EntityNotFoundException.class)
                .thenReturn(tag);

        assertThrows(DuplicateEntityException.class, () -> tagService.addTagToPost(user, post, tag));
    }



    @Test
    public void removeTagFromPost_Should_RemoveTag_When_TagExist() {

        User user = HelperClass.createMockUserUser();

        Post post = HelperClass.createPost();
        post.setUser(user);

        Tag tag = HelperClass.createTag();
        tag.setPosts(new HashSet<>());
        tag.getPosts().add(post);

        post.setTagsOfThePost(new HashSet<>());
        post.getTagsOfThePost().add(tag);

        tagService.removeTagFromPost(user, post, tag);

        assertFalse(post.getTagsOfThePost().contains(tag));
        verify(mockPostRepository, times(1)).updatePost(post);
    }


    @Test
    public void removeTagFromPost_Should_Throw_When_TagDoesNotExist() {

        User user = HelperClass.createMockUserUser();

        Post post = HelperClass.createPost();
        post.setUser(user);

        Tag tag = HelperClass.createTag();
        tag.setPosts(new HashSet<>());

        post.setTagsOfThePost(new HashSet<>());

        assertThrows(EntityNotFoundException.class, () -> tagService.removeTagFromPost(user, post, tag));
    }


}



