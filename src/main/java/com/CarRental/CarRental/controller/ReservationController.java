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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReservationController {


    private CarService carService;
    private ReservationService reservationService;

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(CarService carService, ReservationService reservationService,
                                 UserRepository userRepository,
                                 ReservationRepository reservationRepository) {
        this.carService = carService;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    //handler method to show reservation form
    @GetMapping("/car/{carUrl}/reservation")
    public String showReservationForm(Model model, @PathVariable("carUrl") String carUrl) {
        ReservationDto reservation = new ReservationDto();
        CarDto car = carService.findCarByUrl(carUrl);
        model.addAttribute("reservation", reservation);
        model.addAttribute("car", car); // Dodaj ten wiersz

        return "rentCar/view_car";
    }

    //handler method to handle check the availability of the car and possible reservation
    @PostMapping("/car/{carUrl}/reservation/makereservation")
    public String makeReservation(@PathVariable("carUrl") String carUrl,
                                  @ModelAttribute("reservation") ReservationDto reservationDto,
                                  Model model, RedirectAttributes redirectAttributes) {

        Long carId = carService.findCarByUrl(carUrl).getID();
        LocalDate startDate = reservationDto.getStartDate();
        LocalDate endDate = reservationDto.getEndDate();
        boolean available = reservationService.isCarAvailableForReservation(carId, startDate, endDate);

        if (available) {
            reservationService.createReservation(reservationDto, carUrl);
            redirectAttributes.addAttribute("success", "true");
        } else {
            redirectAttributes.addAttribute("error", "true");
        }
        return "redirect:/car/{carUrl}/reservation";
    }

    @GetMapping("/my_reservations")
    public String myReservationShowForm(Model model) {
        String email = SecurityUtils.getCurrentUser().getUsername();
        System.out.println("Current user email: " + email);
        Long userId = userRepository.findByEmail(email).getId();
        System.out.println("User ID: " + userId);
        List<Reservation> reservations = reservationService.getUserReservationsByUserId(userId);
        System.out.println("Reservations count: " + reservations.size());
        model.addAttribute("reservations", reservations);
        return "rentCar/my_reservations";
    }


    //handler method to delete reservation by a logged-in user
    @GetMapping("/my_reservations/{userId}/delete/{reservationId}")
    public String deleteReservation(@PathVariable("userId") Long userId, @PathVariable("reservationId") Long reservationId) {

        User user = userRepository.findById(userId).orElse(null);

        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null && reservation.getUser_id().equals(user)) {

            reservationService.deleteReservation(reservationId);
        }

        return "redirect:/my_reservations";
    }



}






