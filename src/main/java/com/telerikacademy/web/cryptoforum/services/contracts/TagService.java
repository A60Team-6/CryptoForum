package com.telerikacademy.web.cryptoforum.services.contracts;


import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import jakarta.transaction.Transactional;

public interface TagService {
    Tag getById(int id);

    Tag getTagByTagName(String tagName);

    void createNewTag(Tag tag);

    void addTagToPost(User user, Post post, Tag tag);

    void removeTagFromPost(User user, Post post, Tag tag);

    void deleteTag(Tag tag, User user);
}
