package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.models.Language;
import com.scaler.bookmyshow.models.SeatType;
import com.scaler.bookmyshow.models.Show;
import com.scaler.bookmyshow.services.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

@Controller
public class ShowController {

    private final ShowService showService;
    private static final Logger logger = LoggerFactory.getLogger(ShowController.class);

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    public Show createShow(
            Long movieId,
            Date startTime,
            Date endTime,
            Long auditoriumId,
            Map<SeatType, Integer> seatPricing,
            Language language
    ) {
        logger.info("Creating show for movieId: {}, auditoriumId: {}, language: {}, startTime: {}, endTime: {}",
                movieId, auditoriumId, language, startTime, endTime);
        Show show = showService.createShow(
                movieId, startTime, endTime, auditoriumId, seatPricing, language);
        logger.info("Show created successfully with id: {}", show.getId());
        return show;
    }
}
