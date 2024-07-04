package com.CarRental.CarRental.service.ServiceImpl;



import com.CarRental.CarRental.dto.RegistrationDto;
import com.CarRental.CarRental.entity.Role;
import com.CarRental.CarRental.entity.User;
import com.CarRental.CarRental.repository.RoleRepository;
import com.CarRental.CarRental.repository.UserRepository;
import com.CarRental.CarRental.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setName(registrationDto.getName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setPesel(registrationDto.getPesel());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role=roleRepository.findByName("ROLE_GUEST");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findByPesel(String pesel) {
        return userRepository.findByPesel(pesel);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUsersById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        return userRepository.findUsersByRolesId(roleId);
    }


    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

}