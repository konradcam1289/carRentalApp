# Rent Car App

The "Rent Car App" is a web application designed to manage a car rental service. It offers a variety of features for car management, reservations, user accounts, and administration.

## Features

### Car Management
- **Retrieve All Cars**
  - `GET /admin/cars`: Retrieve a list of all cars and display them in the admin car view.
  
- **Add a New Car**
  - `GET /admin/cars/newcar`: Display the form to add a new car in the admin create car view.
  - `POST /admin/cars`: Create a new car based on the submitted form data in the admin create car view.
  
- **Edit Car Details**
  - `GET /admin/cars/{carID}/edit`: Display the form to edit the details of a specific car in the admin edit car view.
  - `POST /admin/cars/{carID}`: Update the details of a specific car based on the submitted form data in the admin edit car view.
  
- **Delete a Car**
  - `GET /admin/cars/{carID}/delete`: Delete a specific car from the system.

### Car Reservations
- **View Car Details**
  - `GET /car/{carUrl}`: Display detailed information about a specific car, including the option to make a reservation.
  
- **Make a Reservation**
  - `GET /car/{carUrl}/reservation`: Display the reservation form for a specific car.
  - `POST /car/{carUrl}/reservation/makereservation`: Handle the submission of the reservation form, check car availability, and create a reservation if available.
  
- **View My Reservations**
  - `GET /my_reservations`: Display all reservations made by the logged-in user.
  
- **Delete a Reservation**
  - `GET /my_reservations/{userId}/delete/{reservationId}`: Delete a specific reservation made by the logged-in user.

### User Account Management
- **User Login**
  - `GET /login`: Display the login page.
  
- **User Registration**
  - `GET /register`: Display the registration form for creating a new user account.
  - `POST /register/save`: Handle the submission of the registration form, validate the form data, and create a new user account.
  
- **Admin User Management**
  - `GET /admin/administration_users`: Retrieve a list of guest users and display them in the admin users view.
  - `GET /admin/administration_users/{userId}/delete`: Delete a specific guest user from the system.
  - `GET /admin/administration_users/{userId}/view`: Display detailed information about a specific guest user in the admin view user page.

## Tech Stack
- **Java 17**: Programming language used for implementing the application.
- **Spring Boot**: Framework used for building web applications.
- **Thymeleaf**: HTML template engine for rendering views.
- **Hibernate**: Object-Relational Mapping (ORM) tool for database interaction.
- **MySQL**: Database for storing car and user information.
- **Lombok**: Library for reducing boilerplate code.
- **H2 DB**: In-memory database for testing purposes.
- **Open API**: Specification for documenting the API endpoints.
