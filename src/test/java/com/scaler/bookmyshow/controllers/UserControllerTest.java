package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.CreateUserRequestDto;
import com.scaler.bookmyshow.dtos.CreateUserResponseDto;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldReturnCreateUserResponseDto_WhenValidRequestIsProvided() {
        // Arrange
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setEmail("test@example.com");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        when(userService.createUser(anyString())).thenReturn(mockUser);

        // Act
        CreateUserResponseDto responseDto = userController.createUser(requestDto);

        // Assert
        assertEquals(mockUser, responseDto.getUser());
    }
}
