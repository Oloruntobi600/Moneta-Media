package com.monetamedia.Repositories;

import com.monetamedia.Models.Comment;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

public int save(Comment comment) {
    String sql = "INSERT INTO comments (content, creationdate, postid, userid) VALUES (?, ?, ?, ?)";
    try {
        return jdbcTemplate.update(sql, comment.getContent(), comment.getCreationDate(), comment.getPostId(), comment.getUserId());
    } catch (Exception e) {
        // Log the exception for debugging
        System.err.println("Error saving comment: " + e.getMessage());
        throw e;
    }
}
    public Comment findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, commentRowMapper);
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error finding comment by id: " + e.getMessage());
            throw e;
        }
    }

    public List<Comment> findAll() {
        String sql = "SELECT * FROM comments";
        return jdbcTemplate.query(sql, commentRowMapper);
    }

    public List<Comment> findByPostId(Long postId, int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{postId, size, offset}, commentRowMapper);
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error finding comments by post id: " + e.getMessage());
            throw e;
        }
    }

    public int update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, creationdate = ?, postid = ?, userid = ? WHERE id = ?";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getCreationDate(), comment.getPostId(), comment.getUserId(), comment.getId());
    }

    public int delete(Long userid) {
        String sql = "DELETE FROM comments WHERE userid = ?";
        return jdbcTemplate.update(sql, userid);
    }

    public List<Comment> findAll(int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM comments ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, commentRowMapper);
    }

    private RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setContent(rs.getString("content"));
        comment.setCreationDate(rs.getTimestamp("creationdate").toLocalDateTime());
        comment.setPostId(rs.getLong("postid"));
        comment.setUserId(rs.getLong("userid"));
        return comment;
    };
}

