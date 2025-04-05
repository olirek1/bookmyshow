package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.models.City;
import com.scaler.bookmyshow.services.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCity_ShouldReturnCity_WhenNameIsProvided() {

        String cityName = "New York";
        City mockCity = new City();
        mockCity.setName(cityName);
        when(cityService.addCity(anyString())).thenReturn(mockCity);

        City result = cityController.addCity(cityName);

        assertEquals(cityName, result.getName());
    }
}
