package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserService initialized with UserRepository");
    }

    public User createUser(String email) {
        logger.info("Creating user with email: {}", email);

        User user = new User();
        user.setEmail(email);

        User savedUser = userRepository.save(user);
        logger.info("User created successfully with id: {} and email: {}", savedUser.getId(), savedUser.getEmail());

        return savedUser;
    }
}
