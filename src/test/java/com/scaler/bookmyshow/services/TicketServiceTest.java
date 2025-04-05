package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

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

        ShowSeat showSeat1 = new ShowSeat();
        showSeat1.setId(1L);
        showSeat1.setState(ShowSeatState.AVAILABLE);

        ShowSeat showSeat2 = new ShowSeat();
        showSeat2.setId(2L);
        showSeat2.setState(ShowSeatState.AVAILABLE);

        ShowSeat showSeat3 = new ShowSeat();
        showSeat3.setId(3L);
        showSeat3.setState(ShowSeatState.AVAILABLE);

        List<ShowSeat> showSeats = Arrays.asList(showSeat1, showSeat2, showSeat3);

        Show show = new Show();
        show.setId(showId);

        Ticket mockTicket = new Ticket();
        mockTicket.setTicketStatus(TicketStatus.SUCCESS);
        mockTicket.setShowSeats(showSeats);
        mockTicket.setShow(show);

        when(showSeatRepository.findByIdIn(showSeatIds)).thenReturn(showSeats);
        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockTicket);

        // Act
        Ticket result = ticketService.bookTicket(showId, showSeatIds, userId);

        // Assert
        assertEquals(TicketStatus.SUCCESS, result.getTicketStatus());
        assertEquals(showSeats, result.getShowSeats());
        assertEquals(show, result.getShow());
        assertEquals(ShowSeatState.BOOKED, showSeat1.getState());
        assertEquals(ShowSeatState.BOOKED, showSeat2.getState());
        assertEquals(ShowSeatState.BOOKED, showSeat3.getState());
    }

    @Test
    void bookTicket_ShouldThrowException_WhenSeatsAreNotAvailable() {
        // Arrange
        Long showId = 1L;
        List<Long> showSeatIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 1L;

        ShowSeat showSeat1 = new ShowSeat();
        showSeat1.setId(1L);
        showSeat1.setState(ShowSeatState.AVAILABLE);

        ShowSeat showSeat2 = new ShowSeat();
        showSeat2.setId(2L);
        showSeat2.setState(ShowSeatState.BOOKED); // This seat is not available

        ShowSeat showSeat3 = new ShowSeat();
        showSeat3.setId(3L);
        showSeat3.setState(ShowSeatState.AVAILABLE);

        List<ShowSeat> showSeats = Arrays.asList(showSeat1, showSeat2, showSeat3);

        when(showSeatRepository.findByIdIn(showSeatIds)).thenReturn(showSeats);

        // Act & Assert
        assertThrows(ShowSeatNotAvailableException.class, () -> {
            ticketService.bookTicket(showId, showSeatIds, userId);
        });
    }
}
