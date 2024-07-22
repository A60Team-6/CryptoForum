package com.telerikacademy.web.cryptoforum.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    private LocalDate createdAt;

    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;


}
