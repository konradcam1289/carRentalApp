package com.CarRental.CarRental.service;



import com.CarRental.CarRental.dto.RegistrationDto;
import com.CarRental.CarRental.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    User findByPesel(String Pesel);

    User findByEmail(String email);

    Optional<User> findUsersById(Long id);

    List<User> getUsersByRoleId(Long roleId);

    void deleteById(Long id);

    User findUserById(Long id);

}
