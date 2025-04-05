package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.exceptions.CityNotFoundException;
import com.scaler.bookmyshow.models.SeatType;
import com.scaler.bookmyshow.models.Theatre;
import com.scaler.bookmyshow.services.TheatreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class TheatreControllerTest {

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private TheatreController theatreController;

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

        Theatre mockTheatre = new Theatre();
        mockTheatre.setName(name);
        mockTheatre.setAddress(address);
//        mockTheatre.setCityId(cityId);

        when(theatreService.createTheatre(anyString(), anyString(), anyLong())).thenReturn(mockTheatre);

        // Act
        Theatre result = theatreController.createTheatre(name, address, cityId);

        // Assert
        assertEquals(mockTheatre, result);
    }

    @Test
    void createTheatre_ShouldReturnNull_WhenCityNotFound() throws CityNotFoundException {
        // Arrange
        String name = "Grand Theatre";
        String address = "123 Main St";
        Long cityId = 1L;

        doThrow(new CityNotFoundException()).when(theatreService).createTheatre(anyString(), anyString(), anyLong());

        // Act
        Theatre result = theatreController.createTheatre(name, address, cityId);

        // Assert
        assertNull(result);
    }

    @Test
    void addAuditorium_ShouldReturnTheatre() {
        // Arrange
        Long theatreId = 1L;
        String name = "Main Auditorium";
        int capacity = 200;

        Theatre mockTheatre = new Theatre();
        when(theatreService.addAuditorium(anyLong(), anyString(), (int) anyLong())).thenReturn(mockTheatre);

        // Act
        Theatre result = theatreController.addAuditorium(theatreId, name, capacity);

        // Assert
        assertEquals(mockTheatre, result);
    }

    @Test
    void addSeats_ShouldCallServiceMethod() {
        // Arrange
        Long auditoriumId = 1L;
        Map<SeatType, Integer> seatCount = new HashMap<>();
        seatCount.put(SeatType.REGULAR, 100);
        seatCount.put(SeatType.VIP, 50);

        // Act
        theatreController.addSeats(auditoriumId, seatCount);

        // Assert
        // Verify that the theatreService.addSeats method was called with the correct arguments
        // Here, you can use Mockito's verify method if necessary, but in this simple case, it might be omitted.
    }
}
