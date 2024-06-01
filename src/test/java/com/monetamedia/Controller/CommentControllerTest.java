package com.monetamedia.Controller;

import com.monetamedia.Models.ApiResponse;
import com.monetamedia.Models.Comment;
import com.monetamedia.Service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        commentService = mock(CommentService.class);
        commentController = new CommentController(commentService);
    }

    @Test
    public void testCreateComment() {
        Comment comment = new Comment();
        when(commentService.createComment(comment)).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.createComment(comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }

    @Test
    public void testGetCommentById() {
        Long id = 1L;
        Comment comment = new Comment();
        when(commentService.getCommentById(id)).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.getCommentById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }

    @Test
    public void testGetAllComments() {
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentService.getAllComments()).thenReturn(comments);

        ResponseEntity<List<Comment>> response = commentController.getAllComments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
    }
    @Test
    public void testGetCommentsByPostId() {
        Long postId = 1L;
        int page = 1;
        int size = 10;
        String sortBy = "createdAt";
        String sortDir = "asc";
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());
        when(commentService.getCommentsByPostId(postId, page, size, sortBy, sortDir)).thenReturn(comments);

        ResponseEntity<List<Comment>> response = commentController.getCommentsByPostId(postId, page, size, sortBy, sortDir);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
    }

    @Test
    public void testUpdateComment() {
        Long id = 1L;
        Comment comment = new Comment();
        when(commentService.updateComment(comment)).thenReturn(comment);

        ResponseEntity<Comment> response = commentController.updateComment(id, comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }

    @Test
    public void testDeleteComment() {
        Long id = 1L;
        ApiResponse apiResponse = new ApiResponse("Comment deleted successfully",true);
        when(commentService.deleteComment(id)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = commentController.deleteComment(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }
}
