package com.monetamedia.Service;

import com.monetamedia.Models.ResponseApi;
import com.monetamedia.Models.User;
import com.monetamedia.Repositories.UserRepository;
import com.monetamedia.Utility.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;
    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
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

    public boolean authenticateUser(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            logger.debug("User found: {}", user);
            logger.debug("User password: {}", user.getPassword());
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            logger.debug("Password matches: {}", matches);
            return matches;
        }
        logger.error("User not found with username: {}", userName);
        return false;
    }
    private String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
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

