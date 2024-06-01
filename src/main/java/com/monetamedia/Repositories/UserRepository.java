package com.monetamedia.Repositories;

import com.monetamedia.Models.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(User user) {
        String sql = "INSERT INTO users (username, email, profilepicture, password) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUserName(), user.getEmail(), user.getProfilePicture(), user.getPassword());
    }

    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, userRowMapper);
    }
    public User findByUserName(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{username}, userRowMapper);
        if (users.isEmpty()) {
            logger.log(Level.SEVERE, "User not found with username: " + username);
            return null;
        } else if (users.size() > 1) {
            logger.log(Level.SEVERE, "Multiple users found with username: " + username);
            // Handle this case according to your application's logic
            throw new IllegalStateException("Multiple users found with username: " + username);
        } else {
            return users.get(0);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    public List<User> findAll(int page, int size, String sortBy, String sortDir, String search) {
        int offset = page * size;
        String sql = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ? ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        String searchQuery = "%" + search + "%";
        return jdbcTemplate.query(sql, new Object[]{searchQuery, searchQuery, size, offset}, userRowMapper);
    }

    public int update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, profilepicture = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getUserName(), user.getEmail(), user.getProfilePicture(), user.getUserId());
    }

    public int delete(Long userId) {
        String sql = "DELETE FROM users WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }
    public int followUser(Long userId, Long followUserId) {
        String sql = "INSERT INTO user_followers (user_id, follower_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, followUserId);
    }

    public int unfollowUser(Long userId, Long followUserId) {
        String sql = "DELETE FROM user_followers WHERE user_id = ? AND follower_id = ?";
        return jdbcTemplate.update(sql, userId, followUserId);
    }

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("userid"));
        user.setUserName(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setProfilePicture(rs.getString("profilepicture"));
        user.setPassword(rs.getString("password"));
        // Map followers and following separately if needed
        return user;
    };
}
