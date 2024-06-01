package com.monetamedia.Service;

import com.monetamedia.Models.ApiResponse;
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
        try {
            commentRepository.save(comment);
            return comment;
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error creating comment: " + e.getMessage());
            throw e;
        }
    }

    public Comment getCommentById(Long id) {
        try {
            return commentRepository.findById(id);
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error getting comment by id: " + e.getMessage());
            throw e;
        }
    }
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByPostId(Long postId,int page, int size, String sortBy, String sortDir) {
        return commentRepository.findByPostId(postId,page,size,sortBy,sortDir);
    }

    public Comment updateComment(Comment comment) {
        commentRepository.update(comment);
        return comment;
    }


    public ApiResponse deleteComment(Long id) {
        int rowsAffected = commentRepository.delete(id);
        if (rowsAffected > 0) {
            return new ApiResponse("Post with ID " + id + " deleted successfully");
        } else {
            return new ApiResponse("Post with ID " + id + " not found");
        }
    }
}

