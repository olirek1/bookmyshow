package com.scaler.bookmyshow;

import com.scaler.bookmyshow.controllers.*;
import com.scaler.bookmyshow.dtos.CreateUserRequestDto;
import com.scaler.bookmyshow.models.Language;
import com.scaler.bookmyshow.models.SeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BookMyShowApplication implements CommandLineRunner {

    // @Autowired
    private UserController userController;
    private CityController cityController;
    private TheatreController theatreController;
    private ShowController showController;
    private TicketController ticketController;


    @Autowired
    public BookMyShowApplication(UserController userController,
                                 CityController cityController,
                                 TheatreController theatreController,
                                 ShowController showController,
                                 TicketController ticketController
    ) {
        this.userController = userController;
        this.cityController = cityController;
        this.theatreController = theatreController;
        this.showController = showController;
        this.ticketController = ticketController;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookMyShowApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setEmail("aloksaumya1@gmail.com");

        // Hardcoded

        // Make a booking
            // 0. Create a city
            // 1. Create a theatre
            // 2. Create an auditorium in the theatre with seats
            // 3. Create a Movie
            // 4. Create a Show

        this.userController.createUser(requestDto);
        this.cityController.addCity("Aurangabad");
        this.theatreController.createTheatre(
                "INOX",
                "telegana",
                1L
        );

        this.theatreController.addAuditorium(1L, "Audi 1", 123);

        Map<SeatType, Integer> seatsForAudi = new HashMap<>();
        seatsForAudi.put(SeatType.VIP, 20);
        seatsForAudi.put(SeatType.GOLD, 100);

        this.theatreController.addSeats(1L, seatsForAudi);

        this.showController.createShow(
                0L,
                new Date(),
                new Date(),
                1L,
                null,
                Language.ENGLISH
        );

        TicketBookRunner ticketBookRunner1 = new TicketBookRunner(
                this.ticketController,
                3L,
                List.of(61L, 62L, 63L),
                1L
        );

        TicketBookRunner ticketBookRunner2 = new TicketBookRunner(
                this.ticketController,
                3L,
                List.of(60L, 61L, 62L),
                1L
        );

        ticketBookRunner1.run();
    }
}
