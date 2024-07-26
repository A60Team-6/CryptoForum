//package com.telerikacademy.web.cryptoforum.services;
//
//import com.telerikacademy.web.cryptoforum.models.Post;
//import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class PostServiceImplTests {
//
//    @Mock
//    PostRepository mockRepository;
//
//    @InjectMocks
//    PostServiceImpl service;
//
//    private Post post;
//
//    @BeforeEach
//    void setUp() {
//        post = new Post();
//        post.setId(1);
//        // Initialize other post properties if needed
//    }
//
//    @Test
//    public void getPostById_ShouldReturnPost_WhenPostExists() {
//        // Arrange
//        Mockito.when(mockRepository.getPostById(post.getId())).thenReturn(post);
//
//        // Act
//        Post result = service.getPostById(post.getId());
//
//        // Assert
//        Assertions.assertEquals(post, result);
//        verify(mockRepository, times(1)).getPostById(post.getId());
//    }
//
//    @Test
//    public void getPostById_Shout_ReturnPost_When_MatchExists() {
//
//        Mockito.when(mockRepository.getPostById(1)).
//                thenReturn(new Post());
//
//        Post result = service.getPostById(1);
//
//        Assertions.assertEquals(1,result.getId());
//    }
//}
