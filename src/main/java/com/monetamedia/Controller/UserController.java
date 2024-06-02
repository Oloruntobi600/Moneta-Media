package com.monetamedia.Controller;

import com.monetamedia.Models.ResponseApi;
import com.monetamedia.Models.User;
import com.monetamedia.Service.UserService;
import com.monetamedia.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.isPresent()){
            System.out.println("username with :" + user.getUserName() + " already exists");
        }
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
    public ResponseEntity<String> followUser(@PathVariable Long id, @PathVariable Long followId) {
        //fun
        userService.followUser(id, followId);
        String username = userService.getUserById(id).getUserName();
        String followUsername = userService.getUserById(followId).getUserName();
        return ResponseEntity.ok(username + " has followed " + followUsername + " successfully");
    }
    @PostMapping("/{id}/unfollow/{unfollowId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long id, @PathVariable Long unfollowId) {
        userService.unfollowUser(id, unfollowId);
        String username = userService.getUserById(id).getUserName();
        String followUsername = userService.getUserById(unfollowId).getUserName();
        return ResponseEntity.ok(username + " has unfollowed " + followUsername + " successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            if (userService.authenticateUser(user.getUserName(), user.getPassword())) {
                UserDetails userDetails = userService.findByUsername(user.getUserName());
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }
}
