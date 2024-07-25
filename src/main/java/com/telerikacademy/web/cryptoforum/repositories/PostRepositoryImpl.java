package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Post getPostById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public void createPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updatePost(Post postForUpdate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(postForUpdate);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deletePost(int id) {
        Post postToDelete = getPostById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> getAll(FilteredPostsOptions filteredPostsOptions){
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filteredPostsOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
                    });

            filteredPostsOptions.getContent().ifPresent(value -> {
                filters.add("content like :content");
                params.put("content", String.format("%%%s%%", value));
            });

            filteredPostsOptions.getMinLikes().ifPresent(value -> {
                filters.add("likes >= :minLikes");
                params.put("minLike", value);
            });

            filteredPostsOptions.getMaxLikes().ifPresent(value -> {
                filters.add("likes <= :maxLikes");
                params.put("maxLike", value);
            });

            filteredPostsOptions.getCreateBefore().ifPresent(value -> {
                filters.add("createdAt <= :createBefore");
                params.put("createBefore", LocalDateTime.parse(value, FORMATTER));

            });

            filteredPostsOptions.getCreateAfter().ifPresent(value -> {
                filters.add("createdAt >= :createAfter");
                params.put("createAfter", LocalDateTime.parse(value, FORMATTER));
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if(!filters.isEmpty()){
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filteredPostsOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(FilteredPostsOptions filteredPostsOptions) {
        if (filteredPostsOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filteredPostsOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "content":
                orderBy = "content";
                break;
            case "likes":
                orderBy = "likes";
                break;
            case "createdAt":
                orderBy = "createdAt";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filteredPostsOptions.getSortOrder().isPresent() && filteredPostsOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    @Override
    public List<Post> getMostLikedPosts(){
        try (Session session = sessionFactory.openSession()){
            String hql = "FROM Post p ORDER BY p.likes DESC";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setMaxResults(10);
            return query.list();
        }

    }

    @Override
    public List<Post> getMostCommentedPosts(){
        try (Session session = sessionFactory.openSession()){
            String hql = "SELECT p FROM Post p LEFT JOIN p.comments c GROUP BY p.id ORDER BY COUNT(c.id) DESC";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostRecentlyCreated(){
        try (Session session = sessionFactory.openSession()){
            String hql = "FROM Post p ORDER BY p.createdAt DESC";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setMaxResults(10);
            return query.list();
        }
    }
}
