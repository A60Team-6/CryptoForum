package com.telerikacademy.web.cryptoforum.helpers;

import com.telerikacademy.web.cryptoforum.models.*;
import com.telerikacademy.web.cryptoforum.models.dtos.*;
import com.telerikacademy.web.cryptoforum.services.contracts.CommentService;
import com.telerikacademy.web.cryptoforum.services.contracts.PostService;
import com.telerikacademy.web.cryptoforum.services.contracts.TagService;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ModelMapper {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public ModelMapper(PostService postService, CommentService commentService, UserService userService, TagService tagService) {
        this.postService = postService;
        this.commentService = commentService;

        this.userService = userService;
        this.tagService = tagService;
    }


    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        return postDto;
    }

    public PostOutDto toOutDto(Post post) {
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

//    public UserOutDto toDto(User user){
//        UserOutDto userDto = new UserOutDto();
//        userDto.setProfilePicture(user.getProfilePhoto());
//        userDto.setUsername(user.getUsername());
//        userDto.setFirstName(user.getFirstName());
//        userDto.setLastName(user.getLastName());
//        userDto.setPassword(user.getPassword());
//        userDto.setEmail(user.getEmail());
//        userDto.setCreatedAt(user.getCreatedAt());
//        return userDto;
//    }

    public User fromDto(RegisterDto registerDto) {

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());
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

    public User fromDto(int id, UserDto dto) {
        User mockUser = new User();

        mockUser.setId(999999999);
        mockUser.setFirstName("Gogo");
        mockUser.setLastName("Tomov");
        mockUser.setUsername("Gotogoto");
        mockUser.setEmail("ggg.ttt@example.com");
        mockUser.setPassword("15975253");
        mockUser.setPosition(new Position(1, "admin"));
        mockUser.setBlocked(false);
        mockUser.setProfilePhoto(null);
        mockUser.setCreatedAt(LocalDateTime.now());


        User user = new User();
        user.setId(id);
        User userRepository = userService.getById(mockUser, id);
        user.setId(userRepository.getId());
        user.setUsername(userRepository.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPosition(userRepository.getPosition());
        user.setBlocked(userRepository.isBlocked());
        user.setProfilePhoto(userRepository.getProfilePhoto());
        user.setCreatedAt(userRepository.getCreatedAt());
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setId(id);
        Comment comment1 = commentService.getCommentById(comment.getId());
        comment.setPost(comment1.getPost());
        comment.setUser(comment1.getUser());
        comment.setCreatedAt(comment1.getCreatedAt());
        comment.setUpdatedAt(comment1.getUpdatedAt());
        return comment;
    }

    public Tag fromDto(int id, TagDto dto) {
        Tag tag = new Tag();
        tag.setId(id);
        Tag repo = tagService.getById(id);
        tag.setName(dto.getName());
        tag.setPosts(repo.getPosts());
        return tag;
    }

    public Tag fromDto(TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return tag;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        return commentDto;
    }

    public CommentMvcDto toMvcDto(Comment comment) {
        CommentMvcDto commentDto = new CommentMvcDto();
        commentDto.setContent(comment.getContent());
        return commentDto;
    }

    public Comment fromDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        return comment;
    }


//    public User fromUpdateAdminDto(int id, AdminUpdateDto dto) {
//        User admin = new User();
//        admin.setId(id);
//        admin.setFirstName(dto.getFirstName());
//        admin.setLastName(dto.getLastName());
//        admin.setEmail(dto.getEmail());
//        admin.setPassword(dto.getPassword());
//        admin.setProfilePhoto(dto.getProfilePhoto());
//        AdminPhone adminPhone = new AdminPhone();
//        adminPhone.setPhoneNumber(dto.getPhoneNumber());
//        adminPhone.setUser(admin);
//    }


    public AdminPhone fromPhoneDto(PhoneNumberDto phoneNumberDto) {
        AdminPhone adminPhone = new AdminPhone();
        adminPhone.setPhoneNumber(phoneNumberDto.getPhoneNumber());
        return adminPhone;
    }

}
