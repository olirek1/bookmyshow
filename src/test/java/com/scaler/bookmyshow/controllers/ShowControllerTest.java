package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.models.Language;
import com.scaler.bookmyshow.models.SeatType;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.services.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ShowControllerTest {

    @Mock
    private ShowService showService;

    @InjectMocks
    private ShowController showController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShow_ShouldReturnShow_WhenValidInputIsProvided() {
        // Arrange
        Long movieId = 1L;
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 7200000); // 2 hours later
        Long auditoriumId = 1L;
        Map<SeatType, Integer> seatPricing = new HashMap<>();
        seatPricing.put(SeatType.REGULAR, 100);
        seatPricing.put(SeatType.VIP, 200);
        Language language = Language.ENGLISH;

        Show mockShow = new Show();
        mockShow.setStartTime(startTime);
        mockShow.setEndTime(endTime);
        mockShow.setLanguage(language);

        when(showService.createShow(anyLong(), any(Date.class), any(Date.class), anyLong(), any(Map.class), any(Language.class)))
                .thenReturn(mockShow);

        // Act
        Show result = showController.createShow(movieId, startTime, endTime, auditoriumId, seatPricing, language);

        // Assert
        assertEquals(mockShow, result);
    }
}
