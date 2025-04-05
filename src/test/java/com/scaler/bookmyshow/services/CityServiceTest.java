package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.models.City;
import com.scaler.bookmyshow.repositories.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCity_ShouldReturnCity_WhenNameIsProvided() {
        // Arrange
        String cityName = "New York";
        City mockCity = new City();
        mockCity.setName(cityName);

        when(cityRepository.save(any(City.class))).thenReturn(mockCity);

        // Act
        City result = cityService.addCity(cityName);

        // Assert
        assertEquals(mockCity, result);
        assertEquals(cityName, result.getName());
    }
}
