package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.AuditoriumRepository;
import com.scaler.bookmyshow.repositories.ShowRepository;
import com.scaler.bookmyshow.repositories.ShowSeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ShowService {
    private final AuditoriumRepository auditoriumRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

    @Autowired
    public ShowService(AuditoriumRepository auditoriumRepository,
                       ShowRepository showRepository,
                       ShowSeatRepository showSeatRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        logger.info("ShowService initialized with AuditoriumRepository, ShowRepository, and ShowSeatRepository");
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

        Show show = new Show();
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        show.setLanguage(language);

        Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElse(null);
        if (auditorium == null) {
            logger.error("Auditorium with id {} not found", auditoriumId);
            return null;
        }
        show.setAuditorium(auditorium);
        Show savedShow = showRepository.save(show);
        logger.info("Show created with id: {}", savedShow.getId());

        List<ShowSeat> savedShowSeats = new ArrayList<>();
        for (Seat seat : auditorium.getSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShow(savedShow);
            showSeat.setSeat(seat);
            showSeat.setState(ShowSeatState.AVAILABLE);
            savedShowSeats.add(showSeatRepository.save(showSeat));
        }

        savedShow.setShowSeats(savedShowSeats);
        Show finalShow = showRepository.save(savedShow);
        logger.info("Show seats added to show with id: {}", finalShow.getId());

        return finalShow;
    }
}
