package com.scaler.bookmyshow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class ShowSeat extends BaseModel {
    @ManyToOne
    private Show show;
    @ManyToOne
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private ShowSeatState state;
}

// Make a booking
// 0. Create a city
// 1. Create a theatre
// 2. Create an auditorium in the theatre with seats
// 3. Create a Movie
// 4. Create a Show