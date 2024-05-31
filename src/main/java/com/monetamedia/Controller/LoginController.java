package com.monetamedia.Controller;


import com.monetamedia.Models.User;
import com.monetamedia.Service.MyUserDetailService;
import com.monetamedia.Service.UserService;
import com.monetamedia.Utility.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;
    private final MyUserDetailService myUserDetailService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userService, MyUserDetailService myUserDetailService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.myUserDetailService = myUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        logger.debug("Attempting to log in user: {}", user.getUserName());
        boolean authenticated = userService.authenticateUser(user.getUserName(), user.getPassword());
        if (authenticated) {
            UserDetails userDetails = myUserDetailService.loadUserByUsername(user.getUserName());
            String token = jwtUtil.generateToken(String.valueOf(userDetails));
            logger.debug("User authenticated, token generated: {}", token);
            return ResponseEntity.ok(token);
        } else {
            logger.error("Invalid username or password for user: {}", user.getUserName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
