package com.monetamedia.Controller;

import com.monetamedia.Models.ApiResponse;
import com.monetamedia.Models.Comment;
import com.monetamedia.Service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment comment;

    @BeforeEach
    public void setup() {
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
    }

    @Test
    public void testCreateComment() throws Exception {
        when(commentService.createComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(comment.getContent()))
                .andDo(print());
    }

    @Test
    public void testGetCommentById() throws Exception {
        when(commentService.getCommentById(anyLong())).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(comment.getContent()))
                .andDo(print());
    }

    @Test
    public void testGetAllComments() throws Exception {
        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getAllComments()).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(comment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(comment.getContent()))
                .andDo(print());
    }

    @Test
    public void testGetCommentsByPostId() throws Exception {
        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getCommentsByPostId(anyLong(), any(Integer.class), any(Integer.class), any(String.class), any(String.class))).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/post/{postId}", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDir", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(comment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(comment.getContent()))
                .andDo(print());
    }

    @Test
    public void testUpdateComment() throws Exception {
        when(commentService.updateComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(comment.getContent()))
                .andDo(print());
    }

    @Test
    public void testDeleteComment() throws Exception {
        ApiResponse response = new ApiResponse("Comment deleted successfully", true);
        when(commentService.deleteComment(anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }

    @Test
    public void testDeleteCommentNotFound() throws Exception {
        ApiResponse response = new ApiResponse("Comment not found", false);
        when(commentService.deleteComment(anyLong())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(response.getMessage()))
                .andDo(print());
    }
}
