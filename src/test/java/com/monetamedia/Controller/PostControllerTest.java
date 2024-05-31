package com.monetamedia.Controller;

import com.monetamedia.Models.Post;
import com.monetamedia.Service.PostService;
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

public class PostControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void createPost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        when(postService.createPost(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test content\",\"creationDate\":\"2024-05-28T12:00:00\",\"likesCount\":0,\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void getPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void getAllPosts() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        List<Post> posts = Collections.singletonList(post);
        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    void searchPosts() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        List<Post> posts = Collections.singletonList(post);
        when(postService.searchPosts(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(posts);

        mockMvc.perform(get("/posts/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "creation_date")
                        .param("sortDir", "desc")
                        .param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }

    @Test
    void updatePost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Updated content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        when(postService.updatePost(any(Post.class))).thenReturn(post);

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated content\",\"creationDate\":\"2024-05-28T12:00:00\",\"likesCount\":0,\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isOk());
    }

    @Test
    void likePost() throws Exception {
        mockMvc.perform(post("/posts/1/like")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void unlikePost() throws Exception {
        mockMvc.perform(post("/posts/1/unlike")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPostsByUserId() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContent("Test content");
        post.setCreationDate(LocalDateTime.now());
        post.setLikesCount(0);
        post.setUserId(1L);

        List<Post> posts = Collections.singletonList(post);
        when(postService.getPostsByUserId(anyLong())).thenReturn(posts);

        mockMvc.perform(get("/posts/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"));
    }
}
