package com.monetamedia.Repositories;


import com.monetamedia.Models.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Post post) {
        String sql = "INSERT INTO posts (content, creationdate, likescount, userid) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, post.getContent(), post.getCreationdate(), post.getLikesCount(), post.getUserId());
    }

    public Optional<Post> findById(Long userid) {
        String sql = "SELECT * FROM posts WHERE userid = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userid}, postRowMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";
        return jdbcTemplate.query(sql, postRowMapper);
    }

    public List<Post> findByUserId(Long userId) {
        String sql = "SELECT * FROM posts WHERE userid = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, postRowMapper);
    }

    public int update(Post post) {
        String sql = "UPDATE posts SET content = ?, creationdate = ?, likescount = ?, userid = ? WHERE id = ?";
        return jdbcTemplate.update(sql, post.getContent(), post.getCreationdate(), post.getLikesCount(), post.getUserId(), post.getId());
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
        post.setCreationDate(rs.getTimestamp("creationdate").toLocalDateTime());
        post.setLikesCount(rs.getInt("likescount"));
        post.setUserId(rs.getLong("userid"));
        return post;
    };
    public void addLike(Long postId, Long userId) {
        String sql = "INSERT INTO likes (post_id, userid) VALUES (?, ?)";
        jdbcTemplate.update(sql, postId, userId);
    }

    public void removeLike(Long postId, Long userId) {
        String sql = "DELETE FROM likes WHERE postid = ? AND userid = ?";
        jdbcTemplate.update(sql, postId, userId);
    }

    public int countLikes(Long postId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE postid = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{postId}, Integer.class);
    }
}


