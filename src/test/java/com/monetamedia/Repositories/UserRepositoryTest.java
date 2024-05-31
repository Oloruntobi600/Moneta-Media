package com.monetamedia.Repositories;

import static org.junit.jupiter.api.Assertions.*;


import com.monetamedia.Models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserName() {
        User user = new User();
        user.setUserName("bisola");
        user.setPassword("password");
        userRepository.save(user);

        User foundUser = userRepository.findByUserName("bisola");
        assertNotNull(foundUser);
        assertEquals("bisola", foundUser.getUserName());
    }
}
