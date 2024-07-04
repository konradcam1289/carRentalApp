package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.dto.ReservationDto;
import com.CarRental.CarRental.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class RentCarController {

    private final CarService carService;

    public RentCarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String viewRentCar(Model model) {
        List<CarDto> carResponse = carService.findAllCars();
        model.addAttribute("carResponse", carResponse);
        return "rentCar/rentCar";
    }

    @GetMapping("/car/{carUrl}")
    public String showCar(@PathVariable("carUrl") String carUrl, Model model) {
        CarDto car = carService.findCarByUrl(carUrl);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setTotalCost(BigDecimal.valueOf(car.getPrice()));

        model.addAttribute("reservation", reservationDto);
        model.addAttribute("car", car);
        return "rentCar/view_car";
    }

    @GetMapping("/cars/search")
    public String searchCars(@RequestParam(value = "query") String query, Model model) {
        List<CarDto> cars = carService.searchCar(query);
        model.addAttribute("cars", cars);
        return "rentCar/rentCar";
    }
}
