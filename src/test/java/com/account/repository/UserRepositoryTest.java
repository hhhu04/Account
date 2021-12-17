package com.account.repository;

import com.account.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
//    @Transactional
    void creat(){
        User user = new User();
        user.setEmail("test@test");
        user.setPassword("1234");
        user.setRole("ROLES_USER");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

    }



}