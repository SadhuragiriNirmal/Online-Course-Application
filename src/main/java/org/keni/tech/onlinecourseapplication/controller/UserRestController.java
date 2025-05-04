package org.keni.tech.onlinecourseapplication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.keni.tech.onlinecourseapplication.dto.AdminDto;
import org.keni.tech.onlinecourseapplication.dto.CustomerDto;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.service.JwtService;
import org.keni.tech.onlinecourseapplication.service.UserService;
import org.keni.tech.onlinecourseapplication.util.ResponseStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;
    private final JwtService jwtService;

    public UserRestController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    //customer operations
    @PostMapping("customer/signup")
    public ResponseEntity<ResponseStructure<CustomerDto>> saveCustomer(@RequestBody User user) {
        log.info("customer registration started");
        User customer = userService.saveCustomer(user);
        CustomerDto customerDto = userService.customerDtoConverter(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<>("Account Created Successfully", HttpStatus.CREATED.value(), customerDto));
    }

    @PostMapping("customer/login")
    public ResponseEntity<ResponseStructure<CustomerDto>> customerLogin(@RequestBody User user, HttpServletResponse response) {
        User customer = userService.login(user.getEmail(), user.getPassword());
        CustomerDto customerDto = userService.customerDtoConverter(customer);
        Cookie cookie = new Cookie("JWT", jwtService.getToken());
        cookie.setHttpOnly(true); // Prevents JavaScript access
        cookie.setPath("/"); // Cookie is valid for the entire application
        cookie.setMaxAge(86400); // 1 day
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new ResponseStructure<>("Welcome! Login Successful", HttpStatus.OK.value(), customerDto));
    }

    @PutMapping("customer/update")
    public ResponseEntity<ResponseStructure<CustomerDto>> updateCustomer(@RequestBody User user) {
        User customer = userService.updateCustomer(user);
        CustomerDto customerDto = userService.customerDtoConverter(customer);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("Account Updated Successfully", HttpStatus.ACCEPTED.value(), customerDto));

    }

    @DeleteMapping("customer/delete")
    public ResponseEntity<ResponseStructure<CustomerDto>> deleteCustomer() {
        User customer = userService.deleteCustomer();
        CustomerDto customerDto = userService.customerDtoConverter(customer);
        return ResponseEntity.ok().body(new ResponseStructure<>("Customer Deleted Successfully", HttpStatus.OK.value(), customerDto));
    }

    @PostMapping("admin/signup")
    public ResponseEntity<ResponseStructure<AdminDto>> saveAdmin(@RequestBody User user) {
        User admin = userService.saveAdmin(user);
        AdminDto adminDto = userService.adminDtoConverter(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<>("Account Created Successfully", HttpStatus.CREATED.value(), adminDto));
    }

    @PostMapping("admin/login")
    public ResponseEntity<ResponseStructure<AdminDto>> adminLogin(@RequestBody User user, HttpServletResponse response) {
        User admin = userService.login(user.getEmail(), user.getPassword());
        AdminDto adminDto = userService.adminDtoConverter(admin);
        Cookie cookie = new Cookie("JWT", jwtService.getToken());
        cookie.setHttpOnly(true); // Prevents JavaScript access
        cookie.setPath("/"); // Cookie is valid for the entire application
        cookie.setMaxAge(86400); // 1 day
        response.addCookie(cookie);
        return ResponseEntity.ok().body(new ResponseStructure<>("Account Login Successful", HttpStatus.FOUND.value(), adminDto));
    }

    @PutMapping("admin/update")
    public ResponseEntity<ResponseStructure<AdminDto>> updateAdmin(@RequestBody User user) {
        User admin = userService.updateAdmin(user);
        AdminDto adminDto = userService.adminDtoConverter(admin);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("Account Update Successfully", HttpStatus.ACCEPTED.value(), adminDto));
    }

    @DeleteMapping("admin/delete")
    public ResponseEntity<ResponseStructure<AdminDto>> deleteAdmin() {
        User admin = userService.deleteAdmin();
        AdminDto adminDto = userService.adminDtoConverter(admin);
        return ResponseEntity.ok().body(new ResponseStructure<>("Admin Deleted Successfully", HttpStatus.OK.value(), adminDto));
    }
}