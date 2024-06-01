package com.monetamedia.Service;

import com.monetamedia.Models.ResponseApi;
import com.monetamedia.Models.User;
import com.monetamedia.Repositories.UserRepository;
import com.monetamedia.Utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final MyUserDetailService myUserDetailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MyUserDetailService myUserDetailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailService = myUserDetailService;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<User> getUsers(int page, int size, String sortBy, String sortDir, String search) {
        return userRepository.findAll(page, size, sortBy, sortDir, search);
    }

    public User updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.update(user);
        return user;
    }
    public boolean deleteUser(Long id) {
    try {
        int rowsAffected = userRepository.delete(id);
        return rowsAffected > 0;
    } catch (Exception e) {
        return false;
        }
    }

    public void followUser(Long userId, Long followUserId) {
        userRepository.followUser(userId, followUserId);
    }

    public void unfollowUser(Long userId, Long unfollowUserId) {
        userRepository.unfollowUser(userId, unfollowUserId);
    }

    public boolean authenticateUser(String username, String password) {
        try {
            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            return userDetails != null && passwordEncoder.matches(password, userDetails.getPassword());
        } catch (Exception e) {
            return false;
        }
    }

    public UserDetails findByUsername(String username) {
        User user = userRepository.findByUserName(username);
        return new CustomUserDetails(user);
    }

    public ResponseApi markAttendance(HttpServletRequest request) {
        Optional<User> user = jwtUtil.resolveUser(request);
        if (user.isPresent()) {
            // Attendance marking logic
            System.err.println(user.get());
            ResponseApi response = new ResponseApi();
            response.setMessage("Attendance marked successfully for user: " + user.get().getUserName());
            response.setStatus("success");
            return response;
        } else {
            ResponseApi response = new ResponseApi();
            response.setMessage("User not found");
            response.setStatus("error");
            return response;
        }
    }
}

