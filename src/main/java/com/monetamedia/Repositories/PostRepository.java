package com.monetamedia.Repositories;


import com.monetamedia.Models.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Post post) {
        String sql = "INSERT INTO posts (content, creation_date, likes_count, user_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, post.getContent(), post.getCreationDate(), post.getLikesCount(), post.getUserId());
    }

    public Post findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, postRowMapper);
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";
        return jdbcTemplate.query(sql, postRowMapper);
    }

    public List<Post> findByUserId(Long userId) {
        String sql = "SELECT * FROM posts WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, postRowMapper);
    }

    public int update(Post post) {
        String sql = "UPDATE posts SET content = ?, creation_date = ?, likes_count = ?, user_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, post.getContent(), post.getCreationDate(), post.getLikesCount(), post.getUserId(), post.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    public List<Post> findAll(int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM posts ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, postRowMapper);
    }
    public List<Post> searchPosts(String query, int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM posts WHERE content LIKE ? ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        String searchQuery = "%" + query + "%";
        return jdbcTemplate.query(sql, new Object[]{searchQuery, size, offset}, postRowMapper);
    }

    public List<Post> findByUserId(Long userId, int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{userId, size, offset}, postRowMapper);
    }

    private RowMapper<Post> postRowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setContent(rs.getString("content"));
        post.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        post.setLikesCount(rs.getInt("likes_count"));
        post.setUserId(rs.getLong("user_id"));
        return post;
    };
}


