
package com.quiz.user.dao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quiz.user.entities.User;

@SpringBootTest
public class UserDaoTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void testFindByEmail_Existing() {
        User user = new User();
        user.setEmail("test1@gmail.com");
        user.setName("Snehasish");
        user.setPassword("pass");
        userDao.save(user);

        Optional<User> result = userDao.findByEmail("test1@gmail.com");

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    public void testFindByEmail_NonExisting() {
        Optional<User> result = userDao.findByEmail("hello@example.com");

        assertFalse(result.isPresent());
    }
    
    @AfterEach
    public void clearAll() {
    	userDao.deleteAll();
    }
}
