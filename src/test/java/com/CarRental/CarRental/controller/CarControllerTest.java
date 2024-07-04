package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    public void testGetAllCars() throws Exception {
        CarDto car1 = new CarDto();
        car1.setID(1L); // Zmienione na setID
        car1.setMark("Toyota");
        car1.setModel("Corolla");

        CarDto car2 = new CarDto();
        car2.setID(2L); // Zmienione na setID
        car2.setMark("Honda");
        car2.setModel("Civic");

        List<CarDto> carList = Arrays.asList(car1, car2);

        when(carService.findAllCars()).thenReturn(carList);

        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", carList));
    }

    @Test
    public void testCreateCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setMark("Toyota");
        carDto.setModel("Corolla");
        carDto.setProductionYear(2021);

        mockMvc.perform(post("/admin/cars")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("mark", "Toyota")
                        .param("model", "Corolla")
                        .param("productionYear", "2021"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars"));

        verify(carService, times(1)).createCar(any(CarDto.class));
    }

    @Test
    public void testEditCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setID(1L); // Zmienione na setID
        carDto.setMark("Toyota");
        carDto.setModel("Corolla");

        when(carService.findCarByID(1L)).thenReturn(carDto);

        mockMvc.perform(get("/admin/cars/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit_car"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", carDto));
    }

    @Test
    public void testUpdateCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setID(1L); // Zmienione na setID
        carDto.setMark("Toyota");
        carDto.setModel("Corolla");

        mockMvc.perform(post("/admin/cars/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("mark", "Toyota")
                        .param("model", "Corolla")
                        .param("productionYear", "2021"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars"));

        verify(carService, times(1)).updateCar(any(CarDto.class));
    }

    @Test
    public void testDeleteCar() throws Exception {
        mockMvc.perform(get("/admin/cars/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars"));

        verify(carService, times(1)).deleteCar(1L);
    }

    @Test
    public void testShowCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setMark("Toyota");
        carDto.setModel("Corolla");

        when(carService.findCarByUrl("toyota-corolla-2021")).thenReturn(carDto);

        mockMvc.perform(get("/admin/cars/toyota-corolla-2021/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/view_car"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", carDto));
    }
}
