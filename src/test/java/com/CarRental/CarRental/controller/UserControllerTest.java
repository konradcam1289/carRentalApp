package com.CarRental.CarRental.controller;

import com.CarRental.CarRental.dto.RegistrationDto;
import com.CarRental.CarRental.entity.User;
import com.CarRental.CarRental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegister_ExistingEmail() throws Exception {
        RegistrationDto userDto = new RegistrationDto();
        userDto.setEmail("test@example.com");
        userDto.setPesel("12345678901");

        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userService.findByEmail("test@example.com")).thenReturn(existingUser);
        when(userService.findByPesel("12345678901")).thenReturn(null);

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).findByPesel("12345678901");
    }

    @Test
    public void testRegister_Success() throws Exception {
        RegistrationDto userDto = new RegistrationDto();
        userDto.setEmail("test@example.com");
        userDto.setPesel("12345678901");
        userDto.setPassword("password");
        userDto.setConfirmPassword("password");

        when(userService.findByEmail("test@example.com")).thenReturn(null);
        when(userService.findByPesel("12345678901")).thenReturn(null);

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/register?success"));

        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userService, times(1)).findByPesel("12345678901");
        verify(userService, times(1)).saveUser(userDto);
    }

    @Test
    public void testFindGuestUsers() throws Exception {
        User user = new User();
        when(userService.getUsersByRoleId(2L)).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/admin/administration_users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/administration_users"))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).getUsersByRoleId(2L);
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(get("/admin/administration_users/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/administration_users"));

        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    public void testShowGuestUser() throws Exception {
        User user = new User();
        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/admin/administration_users/1/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/view_user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));

        verify(userService, times(1)).findUserById(1L);
    }
}
