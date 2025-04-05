package com.scaler.bookmyshow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Auditorium extends BaseModel {
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Seat> seats;
    private int capacity;

    @ElementCollection // M:M
    @Enumerated(EnumType.STRING)
    private List<AuditoriumFeature> auditoriumFeatures;

    @ManyToOne
    private Theatre theatre;
}
