package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import com.telerikacademy.web.cryptoforum.repositories.contracts.TagRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.TagService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository repository;
    private final PostRepository postRepository;

    public TagServiceImpl(TagRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }

    @Override
    public Tag getById(int id) {
        return repository.getTagById(id);
    }

    @Override
    public Tag getTagByTagName(String tagName){
        return repository.getTagByTagName(tagName);
    }


    @Override
    public void createNewTag(Tag tag){
        boolean duplicateExists = true;
        try {
            repository.getTagByTagName(tag.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Tag", "name", tag.getName());
        }

        repository.createTag(tag);
    }

    @Override
    public void deleteTag(Tag tag, User user){
        PermissionHelper.isAdmin(user, "You are not admin or owner of the publication!");

        repository.deleteTag(tag);
    }

    @Override
    @Transactional
    public void addTagToPost(User user, Post post, Tag tag){
        PermissionHelper.isBlocked(user, "This user is blocked!");
        PermissionHelper.isAdminOrSameUser(user, post.getUser(), "This user is not admin or owner!");


        try {
            repository.getTagByTagName(tag.getName());
        } catch (EntityNotFoundException e) {
            createNewTag(tag);
        }

        Tag tagToAdd = repository.getTagByTagName(tag.getName());
        if(!post.getTagsOfThePost().contains(tagToAdd)){
            post.getTagsOfThePost().add(tagToAdd);
            postRepository.updatePost(post);

        }else {
            throw new DuplicateEntityException("Tag", "name", tag.getName());
        }

    }

    @Override
    @Transactional
    public void removeTagFromPost(User user, Post post, Tag tag){
        PermissionHelper.isBlocked(user, "This user is blocked!");
        PermissionHelper.isAdminOrSameUser(user, post.getUser(), "This user is not admin or owner!");

        if(post.getTagsOfThePost().contains(tag)){
            post.getTagsOfThePost().remove(tag);
            postRepository.updatePost(post);
        }else {
            throw new EntityNotFoundException("Tag", "name", tag.getName());
        }
    }
}
