package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.CreateUserRequestDto;
import com.scaler.bookmyshow.dtos.CreateUserResponseDto;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        logger.info("UserController initialized with UserService");
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto request) {
        logger.info("Received request to create user with email: {}", request.getEmail());
        User savedUser = userService.createUser(request.getEmail());
        logger.info("User created successfully with email: {}", request.getEmail());

        CreateUserResponseDto response = new CreateUserResponseDto();
        response.setUser(savedUser);
        logger.info("Returning response with created user: {}", savedUser.getEmail());

        return response;
    }
}
