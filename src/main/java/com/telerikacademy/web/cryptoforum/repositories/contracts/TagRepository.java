package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.Tag;

public interface TagRepository {

    Tag getTagById(int id);

    Tag getTagByTagName(String name);

    void createTag(Tag tag);

    void deleteTag(Tag tag);

}
