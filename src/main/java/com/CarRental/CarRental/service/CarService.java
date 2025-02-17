package com.CarRental.CarRental.service;


import com.CarRental.CarRental.dto.CarDto;

import java.util.List;

public interface CarService {

    List<CarDto> findAllCars();

    void createCar(CarDto carDto);

    CarDto findCarByID(Long ID);

    void updateCar(CarDto carDto);


    void deleteCar(Long carID);

    CarDto findCarByUrl(String carUrl);

    List<CarDto> searchCar(String query);
}
