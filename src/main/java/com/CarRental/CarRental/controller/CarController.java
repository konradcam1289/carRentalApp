package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.service.CarService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String cars(Model model) {
        List<CarDto> cars = carService.findAllCars();
        model.addAttribute("cars", cars);
        return "admin/cars";
    }

    @GetMapping("/newcar")
    public String newPostForm(Model model) {
        CarDto carDto = new CarDto();
        model.addAttribute("car", carDto);
        return "admin/create_car";
    }

    @PostMapping
    public String createCar(@Valid @ModelAttribute("car") CarDto carDto,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("car", carDto);
            return "admin/create_car";
        }
        carDto.setUrl(generateUrl(carDto.getMark(), carDto.getModel(), carDto.getProductionYear()));
        carService.createCar(carDto);
        return "redirect:/admin/cars";
    }

    @GetMapping("/{carID}/edit")
    public String editPostForm(@PathVariable("carID") String carID,
                               Model model) {
        Long carIdLong = Long.parseLong(carID);
        CarDto carDto = carService.findCarByID(carIdLong);
        model.addAttribute("car", carDto);
        return "admin/edit_car";
    }

    @PostMapping("/{carID}")
    public String updateCar(@PathVariable("carID") Long carID,
                            @Valid @ModelAttribute("car") CarDto car,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("car", car);
            return "admin/edit_car";
        }
        car.setID(carID);
        carService.updateCar(car);
        return "redirect:/admin/cars";
    }

    @GetMapping("/{carID}/delete")
    public String deleteCar(@PathVariable("carID") Long carID) {
        carService.deleteCar(carID);
        return "redirect:/admin/cars";
    }

    @GetMapping("/{carUrl}/view")
    public String showCar(@PathVariable("carUrl") String carUrl, Model model) {
        CarDto cars = carService.findCarByUrl(carUrl);
        model.addAttribute("cars", cars);
        return "admin/view_car";
    }

    // Metoda do generowania URL
    private static String generateUrl(String mark, String model, int productionYear) {
        String url = mark.toLowerCase().replace(" ", "-") + "-" +
                model.toLowerCase().replace(" ", "-") + "-" + productionYear + "-";

        UUID uuid = UUID.randomUUID();
        String randomString = uuid.toString().replace("-", "").substring(0, 8);

        url += randomString;
        return url;
    }
}
