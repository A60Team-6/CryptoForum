package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment getCommentById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return comment;
        }
    }

    @Override
    public void createComment(Comment comment){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeComment(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(comment);
            session.getTransaction().commit();
        }
    }
}
