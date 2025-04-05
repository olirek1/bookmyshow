package com.scaler.bookmyshow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Theatre extends BaseModel {
    private String name;
    private String address;

    @OneToMany(mappedBy = "theatre", fetch = FetchType.EAGER)
    private List<Auditorium> auditoriums;

    @OneToMany
    private List<Show> upcomingShows;
}
