package com.CarRental.CarRental.service.ServiceImpl;


import com.CarRental.CarRental.dto.CarDto;
import com.CarRental.CarRental.entity.Car;
import com.CarRental.CarRental.mapper.CarMapper;
import com.CarRental.CarRental.repository.CarRepository;
import com.CarRental.CarRental.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    @Override
    public List<CarDto> findAllCars() {
        List<Car> posts = carRepository.findAll();
        return posts.stream().map(CarMapper::mapToCarDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createCar(CarDto carDto) {
        Car car= CarMapper.mapToCar(carDto);
        carRepository.save(car);
    }

    @Override
    public CarDto findCarByID(Long ID) {
        Car car=carRepository.findById(ID).get();
        return CarMapper.mapToCarDto(car);
    }

    @Override
    public void updateCar(CarDto carDto) {
        Car car= CarMapper.mapToCar(carDto);
        carRepository.save(car);
    }

    @Override
    public void deleteCar(Long carID) {
        carRepository.deleteById(carID);
    }

    @Override
    public CarDto findCarByUrl(String carUrl) {
        Car car=carRepository.findByUrl(carUrl).get();
        return CarMapper.mapToCarDto(car);
    }

    @Override
    public List<CarDto> searchCar(String query) {
        List<Car> cars=carRepository.serachCars(query);
        return cars.stream().map(CarMapper::mapToCarDto)
                .collect(Collectors.toList());
    }

}
