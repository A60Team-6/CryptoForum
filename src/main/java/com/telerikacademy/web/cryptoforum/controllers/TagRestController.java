package com.telerikacademy.web.cryptoforum.controllers;

import com.telerikacademy.web.cryptoforum.exceptions.AuthorizationException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.AuthenticationHelper;
import com.telerikacademy.web.cryptoforum.helpers.MapperHelper;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.models.dtos.RegistrationDto;
import com.telerikacademy.web.cryptoforum.models.dtos.TagDto;
import com.telerikacademy.web.cryptoforum.services.contracts.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService tagService;

    private final AuthenticationHelper authenticationHelper;

    private final MapperHelper mapperHelper;

    @Autowired
    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper, MapperHelper mapperHelper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.mapperHelper = mapperHelper;
    }


    @GetMapping("/id/{id}")
    public Tag getTagById(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return tagService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @GetMapping("/name/{tagName}")
    public Tag getTagByTagName(@RequestHeader HttpHeaders headers, @PathVariable String tagName){
        try {
            authenticationHelper.tryGetUser(headers);
            return tagService.getTagByTagName(tagName);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


//    @PostMapping
//    public ResponseEntity<String> createTag(@RequestHeader HttpHeaders headers, @Valid @RequestBody TagDto tagDto){
//        try {
//            authenticationHelper.tryGetUser(headers);
//            Tag tag = mapperHelper.createTagFromDto(tagDto);
//            tagService.createNewTag(tag);
//            return new ResponseEntity<>("Congratulations! Tag has been successfully created!", HttpStatus.CREATED);
//        }catch (UnauthorizedOperationException e){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }catch (AuthorizationException e){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeTag(@PathVariable int id, @RequestHeader HttpHeaders headers){
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = tagService.getById(id);
            tagService.deleteTag(tag, user);
            return new ResponseEntity<>("Congratulations! Tag has been successfully deleted!", HttpStatus.OK);
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
