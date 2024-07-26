INSERT INTO positions (name)
VALUES ('admin'),
       ('moderator'),
       ('user');

-- Добавяне на потребители
INSERT INTO users (first_name, last_name, username, password, email, position_id, is_blocked)
VALUES ('John', 'Smith', 'johndoe', 'password123', 'john.doe@example.com', 1, FALSE),
       ('Jane', 'Smith', 'janesmith', 'password123', 'jane.smith@example.com', 1, FALSE),
       ('Alice', 'Johnson', 'alicejohnson', 'password123', 'alice.johnson@example.com', 2, FALSE),
       ('Bobby', 'Williams', 'bobwilliams', 'password123', 'bob.williams@example.com', 3, FALSE),
       ('Charlie', 'Brown', 'charliebrown', 'password123', 'charlie.brown@example.com', 3, FALSE),
       ('David', 'Wilson', 'davidwilson', 'password123', 'david.wilson@example.com', 3, FALSE),
       ('Eveline', 'Davis', 'evadavis', 'password123', 'eva.davis@example.com', 3, FALSE),
       ('Frank', 'Miller', 'frankmiller', 'password123', 'frank.miller@example.com', 3, FALSE),
       ('Grace', 'Taylor', 'gracetaylor', 'password123', 'grace.taylor@example.com', 3, FALSE),
       ('Hank', 'Moore', 'hankmoore', 'password123', 'hank.moore@example.com', 3, FALSE),
       ('Ivet', 'Anderson', 'ivyanderson', 'password123', 'ivy.anderson@example.com', 3, FALSE),
       ('Jack', 'Thomas', 'jackthomas', 'password123', 'jack.thomas@example.com', 3, FALSE),
       ('Kathy', 'Jackson', 'kathyjackson', 'password123', 'kathy.jackson@example.com', 3, FALSE),
       ('Leonardo', 'White', 'leowhite', 'password123', 'leo.white@example.com', 3, FALSE),
       ('Mila', 'Harris', 'miaharris', 'password123', 'mia.harris@example.com', 3, FALSE),
       ('Nate', 'Martin', 'natemartin', 'password123', 'nate.martin@example.com', 3, FALSE),
       ('Olivia', 'Garcia', 'oliviagarcia', 'password123', 'olivia.garcia@example.com', 3, FALSE),
       ('Paul', 'Rodriguez', 'paulrodriguez', 'password123', 'paul.rodriguez@example.com', 3, FALSE),
       ('Quinn', 'Lewis', 'quinnlewis', 'password123', 'quinn.lewis@example.com', 3, FALSE),
       ('Rita', 'Walker', 'ritawalker', 'password123', 'rita.walker@example.com', 3, FALSE);

-- Добавяне на телефонни номера за администратори
INSERT INTO admin_phones (user_id, phone_number)
VALUES (1, '123-456-7890'),
       (2, '987-654-3210');

-- Добавяне на постове
INSERT INTO posts (user_id, title, content, likes, dislikes)
VALUES (1, 'The Future of Crypto Trading',
        'In this post, we will discuss the future trends in crypto trading and how to prepare for them. Get ready to learn about the upcoming technologies and strategies that will shape the market.',
        5, 1),
       (2, 'How to Analyze Crypto Markets',
        'This post covers the fundamental techniques for analyzing cryptocurrency markets, including technical analysis, fundamental analysis, and the importance of market sentiment.',
        3, 0),
       (3, 'Best Practices for Secure Crypto Trading',
        'Learn the best practices for keeping your crypto investments safe from scams, hacks, and other threats. This post includes tips on using secure wallets and avoiding common pitfalls.',
        7, 2),
       (4, 'Top 5 Cryptocurrencies to Watch in 2024',
        'Explore the top five cryptocurrencies that are expected to perform well in 2024. This post provides insights into each cryptocurrency and why it is worth keeping an eye on.',
        8, 3),
       (5, 'Understanding Crypto Trading Bots',
        'Crypto trading bots can help automate your trading strategy. This post explains how these bots work, their benefits, and some popular options available in the market.',
        2, 1);

-- Добавяне на коментари
INSERT INTO comments (post_id, user_id, content)
VALUES (1, 3, 'Great insights! I’m particularly interested in the section about upcoming technologies.'),
       (1, 4, 'I agree, the future looks promising. Thanks for sharing this post.'),
       (2, 5, 'Can you provide more details on technical analysis? It would be helpful.'),
       (2, 6, 'Yes, a deeper dive into technical analysis would be great.'),
       (3, 7, 'Security is crucial in crypto trading. Thanks for highlighting these practices.'),
       (3, 8, 'I found this post very informative. Always good to be aware of potential threats.'),
       (4, 9, 'Interesting list! I’m excited to see how these cryptocurrencies perform.'),
       (4, 10, 'I’m curious about the criteria you used to select these top five cryptocurrencies.'),
       (5, 11, 'Trading bots seem like a good idea. How do I choose the right one?'),
       (5, 12, 'I’m new to trading bots. Any recommendations for beginners?');

-- Добавяне на тагове
INSERT INTO tags (name)
VALUES ('Crypto Trading'),
       ('Market Analysis'),
       ('Security'),
       ('Investment Strategies'),
       ('Trading Bots');

-- Свързване на постове с тагове
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3),
       (4, 1),
       (4, 4),
       (5, 5);

-- Добавяне на реакции на постове
INSERT INTO posts_users_likes (post_id, user_id)
VALUES (1, 3),
       (1, 4),
       (2, 5),
       (3, 7),
       (3, 8),
       (4, 9),
       (4, 10),
       (5, 11),
       (5, 12);
