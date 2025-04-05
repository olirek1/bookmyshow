package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.CityNotFoundException;
import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.AuditoriumRepository;
import com.scaler.bookmyshow.repositories.CityRepository;
import com.scaler.bookmyshow.repositories.SeatRepository;
import com.scaler.bookmyshow.repositories.TheatreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TheatreService {
    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;
    private static final Logger logger = LoggerFactory.getLogger(TheatreService.class);

    @Autowired
    public TheatreService(TheatreRepository theatreRepository,
                          CityRepository cityRepository,
                          AuditoriumRepository auditoriumRepository,
                          SeatRepository seatRepository) {
        this.theatreRepository = theatreRepository;
        this.cityRepository = cityRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.seatRepository = seatRepository;
        logger.info("TheatreService initialized with repositories");
    }

    public Theatre createTheatre(
            String name,
            String address,
            Long cityId
    ) throws CityNotFoundException {
        logger.info("Creating theatre with name: {}, address: {}, cityId: {}", name, address, cityId);
        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isEmpty()) {
            logger.error("City with id {} not found", cityId);
            throw new CityNotFoundException();
        }

        Theatre theatre = new Theatre();
        theatre.setName(name);
        theatre.setAddress(address);
        Theatre savedTheatre = theatreRepository.save(theatre);
        logger.info("Theatre created with id: {}", savedTheatre.getId());

        City dbCity = cityOptional.get();
        if (dbCity.getTheatres() == null) {
            dbCity.setTheatres(new ArrayList<>());
        }
        dbCity.getTheatres().add(savedTheatre);
        cityRepository.save(dbCity);
        logger.info("Theatre with id {} added to city with id {}", savedTheatre.getId(), cityId);

        return savedTheatre;
    }

    public Theatre addAuditorium(Long theatreId, String name, int capacity) {
        logger.info("Adding auditorium with name: {}, capacity: {} to theatreId: {}", name, capacity, theatreId);
        Theatre theatre = theatreRepository.findById(theatreId).orElse(null);
        if (theatre == null) {
            logger.error("Theatre with id {} not found", theatreId);
            return null;
        }

        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setCapacity(capacity);
        auditorium.setTheatre(theatre);
        Auditorium savedAuditorium = auditoriumRepository.save(auditorium);
        logger.info("Auditorium created with id: {}", savedAuditorium.getId());

        theatre.getAuditoriums().add(savedAuditorium);
        Theatre updatedTheatre = theatreRepository.save(theatre);
        logger.info("Auditorium with id {} added to theatre with id {}", savedAuditorium.getId(), theatreId);

        return updatedTheatre;
    }

    public void addSeats(Long auditoriumId, Map<SeatType, Integer> seatCount) {
        logger.info("Adding seats to auditoriumId: {}, seatCount: {}", auditoriumId, seatCount);
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElse(null);
        if (auditorium == null) {
            logger.error("Auditorium with id {} not found", auditoriumId);
            return;
        }

        List<Seat> seats = new ArrayList<>();
        for (Map.Entry<SeatType, Integer> entry : seatCount.entrySet()) {
            for (int i = 0; i < entry.getValue(); ++i) {
                Seat seat = new Seat();
                seat.setSeatType(entry.getKey());
                seat.setSeatNumber(entry.getKey().toString() + (i + 1));
                seats.add(seat);
            }
        }

        List<Seat> savedSeats = seatRepository.saveAll(seats);
        logger.info("Seats saved: {}", savedSeats);

        auditorium.setSeats(savedSeats);
        auditoriumRepository.save(auditorium);
        logger.info("Seats added successfully to auditoriumId: {}", auditoriumId);
    }
}
