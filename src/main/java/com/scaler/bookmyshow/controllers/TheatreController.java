package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.exceptions.CityNotFoundException;
import com.scaler.bookmyshow.models.City;
import com.scaler.bookmyshow.models.SeatType;
import com.scaler.bookmyshow.models.Theatre;
import com.scaler.bookmyshow.services.CityService;
import com.scaler.bookmyshow.services.TheatreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class TheatreController {
    private final TheatreService theatreService;
    private static final Logger logger = LoggerFactory.getLogger(TheatreController.class);

    @Autowired
    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    public Theatre createTheatre(String name, String address, Long cityId) {
        logger.info("Creating theatre with name: {}, address: {}, cityId: {}", name, address, cityId);
        Theatre theatre = null;
        try {
            theatre = this.theatreService.createTheatre(name, address, cityId);
            logger.info("Theatre created successfully with name: {}", name);
        } catch (CityNotFoundException e) {
            logger.error("CityNotFoundException: City with id {} not found", cityId, e);
        }
        return theatre;
    }

    public Theatre addAuditorium(Long theatreId, String name, int capacity) {
        logger.info("Adding auditorium with name: {}, capacity: {} to theatreId: {}", name, capacity, theatreId);
        Theatre theatre = theatreService.addAuditorium(theatreId, name, capacity);
        logger.info("Auditorium added successfully to theatreId: {}", theatreId);
        return theatre;
    }

    /**
     * Adds seats to an auditorium.
     *
     * @param auditoriumId the ID of the auditorium
     * @param seatCount    the map of seat types and their counts
     */
    public void addSeats(Long auditoriumId, Map<SeatType, Integer> seatCount) {
        logger.info("Adding seats to auditoriumId: {}, seatCount: {}", auditoriumId, seatCount);
        theatreService.addSeats(auditoriumId, seatCount);
        logger.info("Seats added successfully to auditoriumId: {}", auditoriumId);
    }
}
