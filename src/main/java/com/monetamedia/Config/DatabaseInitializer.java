package com.monetamedia.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initialize() {
        try {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "userId SERIAL PRIMARY KEY, " +
                    "userName VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "profilePicture VARCHAR(255), " +
                    "password VARCHAR(255) NOT NULL)";

            String createPostsTable = "CREATE TABLE IF NOT EXISTS posts (" +
                    "id SERIAL PRIMARY KEY, " +
                    "content TEXT NOT NULL, " +
                    "creationDate TIMESTAMP NOT NULL, " +
                    "likesCount INT DEFAULT 0, " +
                    "userId INT NOT NULL, " +
                    "FOREIGN KEY (userId) REFERENCES users(userId))";

            String createCommentsTable = "CREATE TABLE IF NOT EXISTS comments (" +
                    "id SERIAL PRIMARY KEY, " +
                    "content TEXT NOT NULL, " +
                    "creationDate TIMESTAMP NOT NULL, " +
                    "postId INT NOT NULL, " +
                    "userId INT NOT NULL, " +
                    "FOREIGN KEY (postId) REFERENCES posts(id), " +
                    "FOREIGN KEY (userId) REFERENCES users(userId))";

            String createUserFollowersTable = "CREATE TABLE IF NOT EXISTS user_followers (" +
                    "user_id INT NOT NULL, " +
                    "follower_id INT NOT NULL, " +
                    "PRIMARY KEY (user_id, follower_id), " +
                    "FOREIGN KEY (user_id) REFERENCES users(userId), " +
                    "FOREIGN KEY (follower_id) REFERENCES users(userId))";

            String createUserUnFollowersTable = "CREATE TABLE IF NOT EXISTS user_unfollowers (" +
                    "user_id INT NOT NULL, " +
                    "unfollower_id INT NOT NULL, " +
                    "PRIMARY KEY (user_id, unfollower_id), " +
                    "FOREIGN KEY (user_id) REFERENCES users(userId), " +
                    "FOREIGN KEY (unfollower_id) REFERENCES users(userId))";

            jdbcTemplate.execute(createUsersTable);
            jdbcTemplate.execute(createPostsTable);
            jdbcTemplate.execute(createCommentsTable);
            jdbcTemplate.execute(createUserFollowersTable);
            jdbcTemplate.execute(createUserUnFollowersTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}