package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.exceptions.ShowSeatNotAvailableException;
import com.scaler.bookmyshow.models.Ticket;
import com.scaler.bookmyshow.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TicketController {
    private final TicketService ticketService;
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public Ticket bookTicket(
            Long showId,
            List<Long> showSeatIds,
            Long userId
    ) throws ShowSeatNotAvailableException {
        logger.info("Attempting to book ticket for showId: {}, userId: {}, showSeatIds: {}", showId, userId, showSeatIds);
        Ticket ticket;
        try {
            ticket = this.ticketService.bookTicket(showId, showSeatIds, userId);
            logger.info("Ticket booked successfully for showId: {}, userId: {}", showId, userId);
        } catch (ShowSeatNotAvailableException e) {
            logger.error("ShowSeatNotAvailableException: One or more seats are not available for showId: {}, userId: {}", showId, userId, e);
            throw e;
        }
        return ticket;
    }
}
