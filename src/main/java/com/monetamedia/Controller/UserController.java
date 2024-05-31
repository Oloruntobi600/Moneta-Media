package com.monetamedia.Controller;

import com.monetamedia.Models.ResponseApi;
import com.monetamedia.Models.User;
import com.monetamedia.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String search) {
        return userService.getUsers(page, size, sortBy, sortDir, search);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setUserId(Math.toIntExact(id));
        return userService.updateUser(user);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    try {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user");
    }
    }
    @PostMapping("/markAttendance")
    public ResponseApi markAttendance(HttpServletRequest request) {
        return userService.markAttendance(request);
    }

    @PostMapping("/{id}/follow/{followId}")
    public void followUser(@PathVariable Long id, @PathVariable Long followId) {
        userService.followUser(id, followId);
    }

    @PostMapping("/{id}/unfollow/{unfollowId}")
    public void unfollowUser(@PathVariable Long id, @PathVariable Long unfollowId) {
        userService.unfollowUser(id, unfollowId);
    }
}