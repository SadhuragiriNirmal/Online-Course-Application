package org.keni.tech.onlinecourseapplication.service;

import lombok.AllArgsConstructor;
import org.keni.tech.onlinecourseapplication.dto.AdminDto;
import org.keni.tech.onlinecourseapplication.dto.CourseDto;
import org.keni.tech.onlinecourseapplication.dto.CustomerDto;
import org.keni.tech.onlinecourseapplication.dto.MyCourseDto;
import org.keni.tech.onlinecourseapplication.exception.MobileNumberLinkedWithOtherAccountException;
import org.keni.tech.onlinecourseapplication.exception.UserAlreadyExistException;
import org.keni.tech.onlinecourseapplication.exception.UserNotFoundException;
import org.keni.tech.onlinecourseapplication.model.Cart;
import org.keni.tech.onlinecourseapplication.model.Role;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.repo.UserRepo;
import org.keni.tech.onlinecourseapplication.util.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepository;
    private final CustomerDto customerDto;
    private final AdminDto adminDto;
    private final CartService cartService;
    private final CourseService courseService;
    private final MyCourseService myCourseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityUtil securityUtil;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User saveCustomer(User user) {
        Optional<User> existCustomer = userRepository.findByEmail(user.getEmail());
        if (existCustomer.isEmpty()) {
            Optional<User> existCustomer_byMobile = userRepository.findByPhoneNumber(user.getPhoneNumber());
            if (existCustomer_byMobile.isEmpty()) {
                user.setRole(Role.CUSTOMER);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setUserName(user.getFirstName());
                User savedUserCustomer = userRepository.save(user);
                Cart cart = cartService.saveCart(savedUserCustomer);
                savedUserCustomer.setCart(cart);
                User updatedCustomer = userRepository.save(savedUserCustomer);
                emailService.sendEmail(user.getEmail(), "Subject: Account Created Successfully", "Welcome to EduTech platform. excel you skill ever.");
                return updatedCustomer;
            } else
                throw new MobileNumberLinkedWithOtherAccountException("The Mobile Number is Linked with Some Other Account");
        } else throw new UserAlreadyExistException("account already exist " + existCustomer.get().getEmail());
    }

    public User saveAdmin(User user) {
        Optional<User> existAdmin = userRepository.findByEmail(user.getEmail());
        if (existAdmin.isEmpty()) {
            Optional<User> existCustomer_byMobile = userRepository.findByPhoneNumber(user.getPhoneNumber());
            if (existCustomer_byMobile.isEmpty()) {
                user.setRole(Role.ADMIN);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setUserName(user.getFirstName());
                return userRepository.save(user);
            } else
                throw new MobileNumberLinkedWithOtherAccountException("The Mobile Number is Linked with Some Other Account");
        } else throw new UserAlreadyExistException("account already exist " + existAdmin.get().getEmail());
    }

    //User Login
    public User login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            if (authentication.isAuthenticated()) {
                Optional<User> user = userRepository.findByEmail(email);
                String token = jwtService.generateToken(authentication.getName());
                System.out.println(token);
                return user.orElse(null);
            }
        } catch (Exception e) {
            throw new UserNotFoundException("account not found");
        }
        return null;
    }


    //update customers
    public User updateCustomer(User user) {
        Optional<User> existCustomerUser = userRepository.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (existCustomerUser.isPresent()) {
            user.setUserId(existCustomerUser.get().getUserId());
            user.setRole(Role.CUSTOMER);
            user.setCart(existCustomerUser.get().getCart());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserName(user.getFirstName());
            user.setMyCourse(existCustomerUser.get().getMyCourse());
            return userRepository.save(user);
        } else throw new UserNotFoundException("User.not found");

    }

    public User updateAdmin(User user) {
        Optional<User> existAdminUser = userRepository.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (existAdminUser.isPresent()) {
            user.setUserId(existAdminUser.get().getUserId());
            user.setRole(Role.ADMIN);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserName(user.getFirstName());
            user.setCourses(existAdminUser.get().getCourses());
            return userRepository.save(user);
        } else throw new UserNotFoundException("User not found");
    }

    //Delete users account

    public User deleteCustomer() {
        Optional<User> existCustomerUser = userRepository.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (existCustomerUser.isPresent()) {
            userRepository.delete(existCustomerUser.get());
            return existCustomerUser.get();
        } else throw new UserNotFoundException("User not found");
    }

    public User deleteAdmin() {
        Optional<User> existAdminUser = userRepository.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (existAdminUser.isPresent()) {
            userRepository.delete(existAdminUser.get());
            return existAdminUser.get();
        } else throw new UserNotFoundException("User not found");
    }

    public CustomerDto customerDtoConverter(User customerUser) {
        customerDto.setCustomerFirstName(customerUser.getFirstName());
        customerDto.setCustomerLastName(customerUser.getLastName());
        customerDto.setCustomerEmail(customerUser.getEmail());
        customerDto.setCustomerPhone(customerUser.getPhoneNumber());
        customerDto.setCustomerName(customerUser.getUserName());
        customerDto.setCartDto(cartService.cartDtoConverter(customerUser.getCart(), customerUser.getCart().getCourseId()));
        if (customerUser.getMyCourse() != null) {
            List<MyCourseDto> myCourseDto = customerUser.getMyCourse().stream().map(myCourseService::myCourseDtoConverter).toList();
            customerDto.setMyCourses(myCourseDto);
        }
        return customerDto;
    }

    public AdminDto adminDtoConverter(User adminUser) {
        adminDto.setAdminFirstName(adminUser.getFirstName());
        adminDto.setAdminLastName(adminUser.getLastName());
        adminDto.setAdminEmail(adminUser.getEmail());
        adminDto.setAdminName(adminUser.getUserName());
        adminDto.setAdminPhone(adminUser.getPhoneNumber());
        if (adminUser.getCourses() != null) {
            List<CourseDto> courseDto = adminUser.getCourses().stream().map(courseService::courseDtoConverter).toList();
            adminDto.setCourses(courseDto);
        }
        return adminDto;
    }
}
