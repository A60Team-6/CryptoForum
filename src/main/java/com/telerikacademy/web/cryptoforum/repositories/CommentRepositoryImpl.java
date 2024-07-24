package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.Comment;
import com.telerikacademy.web.cryptoforum.models.FilteredCommentOptions;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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

    @Override
    public List<Comment> getAll(FilteredCommentOptions filteredCommentOptions){
        try (Session session = sessionFactory.openSession()){
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filteredCommentOptions.getContent().ifPresent(value -> {
                filters.add("content like :content");
                params.put("content", String.format("%%%s%%", value));
            });

            filteredCommentOptions.getCreateBefore().ifPresent(value -> {
                filters.add("createdAt <= :createBefore");
                params.put("createBefore", LocalDateTime.parse(value, FORMATTER));

            });

            filteredCommentOptions.getCreateAfter().ifPresent(value -> {
                filters.add("createdAt >= :createAfter");
                params.put("createAfter", LocalDateTime.parse(value, FORMATTER));
            });

            StringBuilder queryString = new StringBuilder("from Comment");
            if(!filters.isEmpty()){
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filteredCommentOptions));

            Query<Comment> query = session.createQuery(queryString.toString(), Comment.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(FilteredCommentOptions filteredCommentsOptions) {
        if (filteredCommentsOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filteredCommentsOptions.getSortBy().get()) {
            case "content":
                orderBy = "content";
                break;
            case "createdAt":
                orderBy = "createdAt";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filteredCommentsOptions.getSortOrder().isPresent() && filteredCommentsOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
