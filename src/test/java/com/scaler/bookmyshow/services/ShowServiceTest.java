package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.AuditoriumRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ShowServiceTest {

    @Mock
    private AuditoriumRepository auditoriumRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShow_ShouldReturnShow_WhenValidInputsAreProvided() {
        // Arrange
        Long movieId = 1L;
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 7200000); // 2 hours later
        Long auditoriumId = 1L;
        Map<SeatType, Integer> seatPricing = new HashMap<>();
        seatPricing.put(SeatType.REGULAR, 100);
        seatPricing.put(SeatType.VIP, 200);
        Language language = Language.ENGLISH;

        Auditorium mockAuditorium = new Auditorium();
        mockAuditorium.setId(auditoriumId);
        Seat seat1 = new Seat();
        seat1.setId(1L);
        Seat seat2 = new Seat();
        seat2.setId(2L);
        mockAuditorium.setSeats(Arrays.asList(seat1, seat2));

        Show mockShow = new Show();
        mockShow.setId(1L);
        mockShow.setStartTime(startTime);
        mockShow.setEndTime(endTime);
        mockShow.setLanguage(language);
        mockShow.setAuditorium(mockAuditorium);

        ShowSeat mockShowSeat1 = new ShowSeat();
        mockShowSeat1.setId(1L);
        mockShowSeat1.setShow(mockShow);
        mockShowSeat1.setSeat(seat1);
        mockShowSeat1.setState(ShowSeatState.AVAILABLE);

        ShowSeat mockShowSeat2 = new ShowSeat();
        mockShowSeat2.setId(2L);
        mockShowSeat2.setShow(mockShow);
        mockShowSeat2.setSeat(seat2);
        mockShowSeat2.setState(ShowSeatState.AVAILABLE);

        when(auditoriumRepository.findById(anyLong())).thenReturn(Optional.of(mockAuditorium));
        when(showRepository.save(any(Show.class))).thenReturn(mockShow);
        when(showSeatRepository.save(any(ShowSeat.class))).thenReturn(mockShowSeat1, mockShowSeat2);

        // Act
        Show result = showService.createShow(movieId, startTime, endTime, auditoriumId, seatPricing, language);

        // Assert
        assertEquals(mockShow, result);
        assertEquals(2, result.getShowSeats().size());
        assertEquals(ShowSeatState.AVAILABLE, result.getShowSeats().get(0).getState());
    }
}
