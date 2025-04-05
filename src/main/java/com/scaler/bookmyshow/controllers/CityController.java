package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.models.City;
import com.scaler.bookmyshow.services.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CityController {
    private final CityService cityService;
    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    public City addCity(String name) {
        logger.info("Adding city with name: {}", name);
        City city = this.cityService.addCity(name);
        logger.info("City added successfully with name: {}", name);
        return city;
    }
}
