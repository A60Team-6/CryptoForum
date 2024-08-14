package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Position;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.CommentDto;
import com.telerikacademy.web.cryptoforum.models.dtos.PostDto;
import com.telerikacademy.web.cryptoforum.models.dtos.PostOutDto;
import com.telerikacademy.web.cryptoforum.models.dtos.RegisterDto;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ModelMapper {

    private final PostService postService;

    @Autowired
    public ModelMapper(PostService postService) {
        this.postService = postService;
    }


    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }

    public PostDto toDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        return postDto;
    }

    public PostOutDto toOutDto(Post post){
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());
        postOutDto.setUser(post.getUser().getUsername());
        postOutDto.setLikes(post.getLikes());
        postOutDto.setComments(post.getComments().stream().map(Comment::getContent).toList());
        postOutDto.setCreatedAt(post.getCreatedAt());
        postOutDto.setUpdatedAt(post.getUpdatedAt());
        return postOutDto;
    }

    public User fromDto(RegisterDto registerDto) {

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPosition(new Position(3, "user"));

        return user;
    }

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repository = postService.getPostById(id);
        post.setUser(repository.getUser());
        post.setLikes(repository.getLikes());
        post.setCreatedAt(repository.getCreatedAt());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }

    public Comment fromDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        return comment;
    }

}
