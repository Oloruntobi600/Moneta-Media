package com.monetamedia.Controller;

import static org.junit.jupiter.api.Assertions.*;

import com.monetamedia.Models.User;
import com.monetamedia.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setProfilePicture("profile.jpg");
        user.setPassword("password");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\",\"email\":\"test@example.com\",\"profilePicture\":\"profile.jpg\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.profilePicture").value("profile.jpg"));
    }
    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setProfilePicture("profile.jpg");
        user.setPassword("password");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.profilePicture").value("profile.jpg"));
    }

    @Test
    void getAllUsers() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setProfilePicture("profile.jpg");
        user.setPassword("password");

        List<User> users = Collections.singletonList(user);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].userName").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].profilePicture").value("profile.jpg"));
    }

    @Test
    void searchUsers() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setProfilePicture("profile.jpg");
        user.setPassword("password");

        List<User> users = Collections.singletonList(user);
        when(userService.getUsers(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(users);

        mockMvc.perform(get("/users/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "username")
                        .param("sortDir", "asc")
                        .param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].userName").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].profilePicture").value("profile.jpg"));
    }

    @Test
    void updateUser() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserName("updateduser");
        user.setEmail("updated@example.com");
        user.setProfilePicture("updated.jpg");
        user.setPassword("updatedpassword");

        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"updateduser\",\"email\":\"updated@example.com\",\"profilePicture\":\"updated.jpg\",\"password\":\"updatedpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.profilePicture").value("updated.jpg"));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void followUser() throws Exception {
        doNothing().when(userService).followUser(1L, 2L);

        mockMvc.perform(post("/users/1/follow/2"))
                .andExpect(status().isOk());
    }

    @Test
    void unfollowUser() throws Exception {
        doNothing().when(userService).unfollowUser(1L, 2L);

        mockMvc.perform(post("/users/1/unfollow/2"))
                .andExpect(status().isOk());
    }
}
