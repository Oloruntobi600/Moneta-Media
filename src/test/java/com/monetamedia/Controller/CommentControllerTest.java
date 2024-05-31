package com.monetamedia.Controller;

import com.monetamedia.Models.Comment;
import com.monetamedia.Service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void createComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setCreationDate(LocalDateTime.now());
        comment.setPostId(1L);
        comment.setUserId(1L);

        when(commentService.createComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test content\",\"creationDate\":\"2024-05-28T12:00:00\",\"postId\":1,\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void getCommentById() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setCreationDate(LocalDateTime.now());
        comment.setPostId(1L);
        comment.setUserId(1L);

        when(commentService.getCommentById(1L)).thenReturn(comment);

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void getAllComments() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setCreationDate(LocalDateTime.now());
        comment.setPostId(1L);
        comment.setUserId(1L);

        List<Comment> comments = Collections.singletonList(comment);
        when(commentService.getAllComments()).thenReturn(comments);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    void getCommentsByPostId() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test content");
        comment.setCreationDate(LocalDateTime.now());
        comment.setPostId(1L);
        comment.setUserId(1L);

        List<Comment> comments = Collections.singletonList(comment);
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        mockMvc.perform(get("/comments/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    void updateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Updated content");
        comment.setCreationDate(LocalDateTime.now());
        comment.setPostId(1L);
        comment.setUserId(1L);

        when(commentService.updateComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated content\",\"creationDate\":\"2024-05-28T12:00:00\",\"postId\":1,\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isOk());
    }
}
