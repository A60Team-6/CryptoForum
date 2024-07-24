create database forum;
use forum;

CREATE TABLE positions
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(32)        NOT NULL,
    last_name     VARCHAR(32)        NOT NULL,
    username      VARCHAR(32) UNIQUE NOT NULL,
    password      VARCHAR(256)       NOT NULL,
    email         VARCHAR(64) UNIQUE NOT NULL,
    profile_photo VARCHAR(256) DEFAULT NULL,
    position_id   INT                NOT NULL,
    is_blocked    BOOLEAN      DEFAULT FALSE,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CHECK (CHAR_LENGTH(first_name) BETWEEN 4 AND 32),
    CHECK (CHAR_LENGTH(last_name) BETWEEN 4 AND 32),
    FOREIGN KEY (position_id) REFERENCES positions (id)

);

CREATE TABLE admin_phones
(
    user_id      INT UNIQUE         NOT NULL,
    phone_number VARCHAR(15) UNIQUE DEFAULT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE posts
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT           NOT NULL,
    title      VARCHAR(64)   NOT NULL,
    content    VARCHAR(8192) NOT NULL,
    likes      INT       DEFAULT 0,
    dislikes   INT       DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (CHAR_LENGTH(posts.title) BETWEEN 16 AND 64),
    CHECK (CHAR_LENGTH(posts.content) BETWEEN 32 AND 8192),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE comments
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT,
    user_id    INT          NOT NULL,
    content    VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    parent_id  INT,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (parent_id) REFERENCES comments (id)
);

CREATE TABLE tags
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE post_tags
(
    post_id INT NOT NULL,
    tag_id  INT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

# CREATE TABLE post_reactions
# (
#     post_id     INT NOT NULL,
#     user_id     INT NOT NULL,
#     reaction    ENUM('like', 'dislike') DEFAULT NULL,
#     PRIMARY KEY (post_id, user_id),
#     FOREIGN KEY (post_id) REFERENCES posts (id),
#     FOREIGN KEY (user_id) REFERENCES users (id)
# );


CREATE TABLE posts_users_likes
(
    post_id    INT NOT NULL,
    user_id     INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);