package com.telerikacademy.web.cryptoforum.repositories;

import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.models.FilteredPostsOptions;
import com.telerikacademy.web.cryptoforum.models.Post;
import com.telerikacademy.web.cryptoforum.models.Tag;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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
    public Post getPostByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, title);
            if (post == null) {
                throw new EntityNotFoundException("Post","title", title);
            }
            return post;
        }
    }

    @Override
    public List<Post> getPostsWithThisUser(User user) {

        try (Session session = sessionFactory.openSession()){
            String hql = "FROM Post p WHERE p.user = :user";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setParameter("user", user);
            return query.list();
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
    public int countFilteredPosts(FilteredPostsOptions filteredPostsOptions) {
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
                params.put("minLikes", value);
            });

            filteredPostsOptions.getMaxLikes().ifPresent(value -> {
                filters.add("likes <= :maxLikes");
                params.put("maxLikes", value);
            });

            filteredPostsOptions.getCreateBefore().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt <= :createBefore");
                    params.put("createBefore", LocalDateTime.parse(value, FORMATTER));
                }
            });

            filteredPostsOptions.getCreateAfter().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt >= :createAfter");
                    params.put("createAfter", LocalDateTime.parse(value, FORMATTER));
                }
            });

            StringBuilder queryString = new StringBuilder("select count(*) from Post");
            if (!filters.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filters));
            }

            Query<Long> query = session.createQuery(queryString.toString(), Long.class);
            query.setProperties(params);

            return Math.toIntExact(query.uniqueResult());
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
                params.put("minLikes", value);
            });

            filteredPostsOptions.getMaxLikes().ifPresent(value -> {
                filters.add("likes <= :maxLikes");
                params.put("maxLikes", value);
            });

            filteredPostsOptions.getCreateBefore().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt <= :createBefore");
                    params.put("createBefore", LocalDateTime.parse(value, FORMATTER));
                }
            });

            filteredPostsOptions.getCreateAfter().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt >= :createAfter");
                    params.put("createAfter", LocalDateTime.parse(value, FORMATTER));
                }
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if(!filters.isEmpty()){
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            //queryString.append(generateOrderBy(filteredPostsOptions));
            String orderByClause = generateOrderBy(filteredPostsOptions);
            if (!orderByClause.isEmpty()) {
                queryString.append(orderByClause);
            }

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Post> getAll(FilteredPostsOptions filteredPostsOptions, int page, int pageSize){
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
                params.put("minLikes", value);
            });

            filteredPostsOptions.getMaxLikes().ifPresent(value -> {
                filters.add("likes <= :maxLikes");
                params.put("maxLikes", value);
            });

            filteredPostsOptions.getCreateBefore().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt <= :createBefore");
                    params.put("createBefore", LocalDateTime.parse(value, FORMATTER));
                }
            });

            filteredPostsOptions.getCreateAfter().ifPresent(value -> {
                if (!value.isBlank()) {
                    filters.add("createdAt >= :createAfter");
                    params.put("createAfter", LocalDateTime.parse(value, FORMATTER));
                }
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if(!filters.isEmpty()){
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            //queryString.append(generateOrderBy(filteredPostsOptions));
            String orderByClause = generateOrderBy(filteredPostsOptions);
            if (!orderByClause.isEmpty()) {
                queryString.append(orderByClause);
            }

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);

            Pageable pageable = PageRequest.of(page, pageSize);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageSize);

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
                break;
            default:
                return "";
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
            List<Post> list = query.list();
            return list;
        }
    }

    @Override
    public void addTagToPost(User user, Post post, Tag tag){

    }

    @Override
    public void deleteTagFromPost(User user, Post post, Tag tag){

    }

}
