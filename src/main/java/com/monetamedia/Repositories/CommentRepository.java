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
        String sql = "INSERT INTO comments (content, creation_date, post_id, user_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getCreationDate(), comment.getPostId(), comment.getUserId());
    }

    public Comment findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, commentRowMapper);
    }

    public List<Comment> findAll() {
        String sql = "SELECT * FROM comments";
        return jdbcTemplate.query(sql, commentRowMapper);
    }

    public List<Comment> findByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, commentRowMapper);
    }

    public int update(Comment comment) {
        String sql = "UPDATE comments SET content = ?, creation_date = ?, post_id = ?, user_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getCreationDate(), comment.getPostId(), comment.getUserId(), comment.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Comment> findAll(int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM comments ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{size, offset}, commentRowMapper);
    }

    public List<Comment> findByPostId(Long postId, int page, int size, String sortBy, String sortDir) {
        int offset = page * size;
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY " + sortBy + " " + sortDir + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{postId, size, offset}, commentRowMapper);
    }

    private RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setContent(rs.getString("content"));
        comment.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        comment.setPostId(rs.getLong("post_id"));
        comment.setUserId(rs.getLong("user_id"));
        return comment;
    };
}

