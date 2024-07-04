package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.dto.ReservationDto;
import com.CarRental.CarRental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class RentCarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private RentCarController rentCarController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentCarController).build();
    }

    @Test
    public void testViewRentCar() throws Exception {
        CarDto car1 = new CarDto();
        car1.setID(1L);
        car1.setMark("Toyota");
        car1.setModel("Corolla");
        car1.setPrice(100);

        CarDto car2 = new CarDto();
        car2.setID(2L);
        car2.setMark("Honda");
        car2.setModel("Civic");
        car2.setPrice(120);

        when(carService.findAllCars()).thenReturn(Arrays.asList(car1, car2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("rentCar/rentCar"))
                .andExpect(model().attributeExists("carResponse"))
                .andExpect(model().attribute("carResponse", Arrays.asList(car1, car2)));

        verify(carService, times(1)).findAllCars();
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void testShowCar() throws Exception {
        CarDto car = new CarDto();
        car.setID(1L);
        car.setMark("Toyota");
        car.setModel("Corolla");
        car.setPrice(100);

        when(carService.findCarByUrl("toyota-corolla-2023")).thenReturn(car);

        mockMvc.perform(get("/car/toyota-corolla-2023"))
                .andExpect(status().isOk())
                .andExpect(view().name("rentCar/view_car"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attribute("car", car))
                .andExpect(model().attribute("reservation", hasProperty("totalCost", is(BigDecimal.valueOf(car.getPrice())))));

        verify(carService, times(1)).findCarByUrl("toyota-corolla-2023");
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void testSearchCars() throws Exception {
        CarDto car1 = new CarDto();
        car1.setID(1L);
        car1.setMark("Toyota");
        car1.setModel("Corolla");
        car1.setPrice(100);

        CarDto car2 = new CarDto();
        car2.setID(2L);
        car2.setMark("Honda");
        car2.setModel("Civic");
        car2.setPrice(120);

        when(carService.searchCar("Toyota")).thenReturn(Arrays.asList(car1));

        mockMvc.perform(get("/cars/search").param("query", "Toyota"))
                .andExpect(status().isOk())
                .andExpect(view().name("rentCar/rentCar"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", Arrays.asList(car1)));

        verify(carService, times(1)).searchCar("Toyota");
        verifyNoMoreInteractions(carService);
    }
}
