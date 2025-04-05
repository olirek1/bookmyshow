package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.scaler.bookmyshow.models.ShowSeat;
import com.scaler.bookmyshow.models.ShowSeatState;
import com.scaler.bookmyshow.models.Ticket;
import com.scaler.bookmyshow.models.TicketStatus;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import com.scaler.bookmyshow.repositories.TicketRepository;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    public TicketService(ShowSeatRepository showSeatRepository, UserRepository userRepository, ShowRepository showRepository, TicketRepository ticketRepository) {
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        logger.info("TicketService initialized with repositories");
    }

    // Main logic for booking
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(
            Long showId,
            List<Long> showSeatIds,
            Long userId
    ) throws ShowSeatNotAvailableException {
        logger.info("Attempting to book ticket for showId: {}, userId: {}, showSeatIds: {}", showId, userId, showSeatIds);

        // Fetch the list of ShowSeat by given showSeatsId
        List<ShowSeat> showSeats = showSeatRepository.findByIdIn(showSeatIds);

        // Check for availability of the seat
        for (ShowSeat showSeat : showSeats) {
            if (!showSeat.getState().equals(ShowSeatState.AVAILABLE)) {
                // Seat is not available, raise an exception
                logger.error("Show seat is not available: {}", showSeat.getId());
                throw new ShowSeatNotAvailableException("Show seat is not available: " + showSeat.getId());
            }
        }

        // If we are here, this means seats are available hence
        // Update the seat status to lock
        for (ShowSeat showSeat : showSeats) {
            showSeat.setState(ShowSeatState.LOCKED);
            showSeatRepository.save(showSeat);
        }
        logger.info("Show seats locked for showId: {}", showId);

        // Assuming payment confirmation done
        // Return the ticket object
        Ticket ticket = new Ticket();
        ticket.setTicketStatus(TicketStatus.SUCCESS);
        ticket.setShowSeats(showSeats);
        ticket.setShow(showRepository.findById(showId).orElse(null));
        ticket = ticketRepository.save(ticket);
        logger.info("Ticket created with id: {} for showId: {}", ticket.getId(), showId);

        // Update the status of showSeats to booked
        for (ShowSeat showSeat : showSeats) {
            showSeat.setState(ShowSeatState.BOOKED);
            showSeatRepository.save(showSeat);
        }
        logger.info("Show seats booked for ticket id: {}", ticket.getId());

        return ticket;
    }

    private List<ShowSeat> checkAvailabilityAndLock(List<Long> showSeatIds) throws ShowSeatNotAvailableException {
        logger.info("Checking availability and locking seats for showSeatIds: {}", showSeatIds);

        // Fetch the given showSeats
        List<ShowSeat> showSeats = showSeatRepository.findByIdIn(showSeatIds);

        // Check for availability
        for (ShowSeat showSeat : showSeats) {
            if (!showSeat.getState().equals(ShowSeatState.AVAILABLE)) {
                logger.error("Show seat is not available: {}", showSeat.getId());
                throw new ShowSeatNotAvailableException("Show seat is not available: " + showSeat.getId());
            }
        }

        // Update the status to lock
        for (ShowSeat showSeat : showSeats) {
            showSeat.setState(ShowSeatState.LOCKED);
            showSeatRepository.save(showSeat);
        }
        logger.info("Show seats locked for showSeatIds: {}", showSeatIds);

        return showSeats;
    }
}
