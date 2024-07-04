package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.dto.ReservationDto;
import com.CarRental.CarRental.entity.Reservation;
import com.CarRental.CarRental.entity.User;
import com.CarRental.CarRental.repository.ReservationRepository;
import com.CarRental.CarRental.repository.UserRepository;
import com.CarRental.CarRental.service.CarService;
import com.CarRental.CarRental.service.ReservationService;
import com.CarRental.CarRental.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReservationControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    public void testShowReservationForm() throws Exception {
        CarDto car = new CarDto();
        car.setUrl("toyota-corolla-2023");
        when(carService.findCarByUrl("toyota-corolla-2023")).thenReturn(car);

        mockMvc.perform(get("/car/toyota-corolla-2023/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("rentCar/view_car"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", car));

        verify(carService, times(1)).findCarByUrl("toyota-corolla-2023");
        verifyNoMoreInteractions(carService);
    }

    @Test
    public void testMakeReservation() throws Exception {
        CarDto car = new CarDto();
        car.setID(1L);
        car.setUrl("toyota-corolla-2023");
        when(carService.findCarByUrl("toyota-corolla-2023")).thenReturn(car);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setStartDate(LocalDate.now());
        reservationDto.setEndDate(LocalDate.now().plusDays(1));

        when(reservationService.isCarAvailableForReservation(1L, reservationDto.getStartDate(), reservationDto.getEndDate())).thenReturn(true);

        mockMvc.perform(post("/car/toyota-corolla-2023/reservation/makereservation")
                        .flashAttr("reservation", reservationDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/toyota-corolla-2023/reservation?success=true"));

        verify(reservationService, times(1)).createReservation(reservationDto, "toyota-corolla-2023");
    }



    @Test
    public void testDeleteReservation() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        Reservation reservation = new Reservation();
        reservation.setUser_id(user);
        when(reservationService.getReservationById(1L)).thenReturn(reservation);

        mockMvc.perform(get("/my_reservations/1/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my_reservations"));

        verify(reservationService, times(1)).deleteReservation(1L);
    }
}
