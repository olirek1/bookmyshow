package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.scaler.bookmyshow.models.Ticket;
import com.scaler.bookmyshow.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookTicket_ShouldReturnTicket_WhenSeatsAreAvailable() throws ShowSeatNotAvailableException {
        // Arrange
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 1L;

        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
//        mockTicket.setShowId(showId);
//        mockTicket.setUserId(userId);
//        mockTicket.setShowSeatIds(showSeatIds);

        when(ticketService.bookTicket(anyLong(), anyList(), anyLong())).thenReturn(mockTicket);

        // Act
        Ticket result = ticketController.bookTicket(showId, showSeatIds, userId);

        // Assert
        assertEquals(mockTicket, result);
    }

    @Test
    void bookTicket_ShouldThrowException_WhenSeatsAreNotAvailable() throws ShowSeatNotAvailableException {
        // Arrange
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 1L;

        when(ticketService.bookTicket(anyLong(), anyList(), anyLong())).thenThrow(new ShowSeatNotAvailableException("Seats not available"));

        // Act & Assert
        assertThrows(ShowSeatNotAvailableException.class, () -> {
            ticketController.bookTicket(showId, showSeatIds, userId);
        });
    }
}
