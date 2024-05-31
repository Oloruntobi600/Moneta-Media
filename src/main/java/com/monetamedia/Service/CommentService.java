package com.monetamedia.Service;

import com.monetamedia.Models.Comment;
import com.monetamedia.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment updateComment(Comment comment) {
        commentRepository.update(comment);
        return comment;
    }

    public void deleteComment(Long id) {
        commentRepository.delete(id);
    }
}

