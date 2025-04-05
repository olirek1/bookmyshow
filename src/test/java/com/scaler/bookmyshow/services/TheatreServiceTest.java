package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.CityNotFoundException;
import com.scaler.bookmyshow.models.*;
import com.scaler.bookmyshow.repositories.AuditoriumRepository;
import com.scaler.bookmyshow.repositories.CityRepository;
import com.scaler.bookmyshow.repositories.SeatRepository;
import com.scaler.bookmyshow.repositories.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private AuditoriumRepository auditoriumRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private TheatreService theatreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTheatre_ShouldReturnTheatre_WhenCityExists() throws CityNotFoundException {
        // Arrange
        String name = "Grand Theatre";
        String address = "123 Main St";
        Long cityId = 1L;

        City mockCity = new City();
        mockCity.setId(cityId);

        Theatre mockTheatre = new Theatre();
        mockTheatre.setName(name);
        mockTheatre.setAddress(address);

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(mockCity));
        when(theatreRepository.save(any(Theatre.class))).thenReturn(mockTheatre);
        when(cityRepository.save(any(City.class))).thenReturn(mockCity);

        // Act
        Theatre result = theatreService.createTheatre(name, address, cityId);

        // Assert
        assertEquals(mockTheatre, result);
    }

    @Test
    void createTheatre_ShouldThrowException_WhenCityDoesNotExist() {
        // Arrange
        String name = "Grand Theatre";
        String address = "123 Main St";
        Long cityId = 1L;

        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CityNotFoundException.class, () -> {
            theatreService.createTheatre(name, address, cityId);
        });
    }

    @Test
    void addAuditorium_ShouldReturnTheatre() {
        // Arrange
        Long theatreId = 1L;
        String name = "Main Auditorium";
        int capacity = 200;

        Theatre mockTheatre = new Theatre();
        mockTheatre.setId(theatreId);
        mockTheatre.setAuditoriums(new ArrayList<>());

        Auditorium mockAuditorium = new Auditorium();
        mockAuditorium.setName(name);
        mockAuditorium.setCapacity(capacity);

        when(theatreRepository.findById(anyLong())).thenReturn(Optional.of(mockTheatre));
        when(auditoriumRepository.save(any(Auditorium.class))).thenReturn(mockAuditorium);
        when(theatreRepository.save(any(Theatre.class))).thenReturn(mockTheatre);

        // Act
        Theatre result = theatreService.addAuditorium(theatreId, name, capacity);

        // Assert
        assertEquals(mockTheatre, result);
        assertEquals(1, result.getAuditoriums().size());
        assertEquals(mockAuditorium, result.getAuditoriums().get(0));
    }

    @Test
    void addSeats_ShouldAddSeatsToAuditorium() {
        // Arrange
        Long auditoriumId = 1L;
        Map<SeatType, Integer> seatCount = new HashMap<>();
        seatCount.put(SeatType.REGULAR, 100);
        seatCount.put(SeatType.VIP, 50);

        Auditorium mockAuditorium = new Auditorium();
        mockAuditorium.setId(auditoriumId);
        mockAuditorium.setSeats(new ArrayList<>());

        List<Seat> mockSeats = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            Seat seat = new Seat();
            seat.setId((long) i + 1);
            seat.setSeatType(i < 100 ? SeatType.REGULAR : SeatType.VIP);
            mockSeats.add(seat);
        }

        when(auditoriumRepository.findById(anyLong())).thenReturn(Optional.of(mockAuditorium));
        when(seatRepository.saveAll(any(List.class))).thenReturn(mockSeats);

        // Act
        theatreService.addSeats(auditoriumId, seatCount);

        // Assert
        assertEquals(150, mockAuditorium.getSeats().size());
        assertEquals(SeatType.REGULAR, mockAuditorium.getSeats().get(0).getSeatType());
        assertEquals(SeatType.VIP, mockAuditorium.getSeats().get(100).getSeatType());
    }
}
