package org.keni.tech.onlinecourseapplication.service;

import lombok.Getter;
import lombok.Setter;
import org.keni.tech.onlinecourseapplication.dto.CartDto;
import org.keni.tech.onlinecourseapplication.dto.CustomerCourseDto;
import org.keni.tech.onlinecourseapplication.exception.CourseAlreadyAddedInTheCartException;
import org.keni.tech.onlinecourseapplication.exception.CourseAlreadyBoughtException;
import org.keni.tech.onlinecourseapplication.exception.CourseNotFoundException;
import org.keni.tech.onlinecourseapplication.exception.UserNotFoundException;
import org.keni.tech.onlinecourseapplication.model.Cart;
import org.keni.tech.onlinecourseapplication.model.Course;
import org.keni.tech.onlinecourseapplication.model.MyCourse;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.repo.CartRepo;
import org.keni.tech.onlinecourseapplication.repo.CourseRepo;
import org.keni.tech.onlinecourseapplication.repo.UserRepo;
import org.keni.tech.onlinecourseapplication.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CartDto cartDto;
    private CourseService courseService;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private SecurityUtil securityUtil;

    public CartService(CartRepo cartRepo, CartDto cartDto, CourseService courseService, UserRepo userRepo, CourseRepo courseRepo, SecurityUtil securityUtil) {
        this.cartRepo = cartRepo;
        this.cartDto = cartDto;
        this.courseService = courseService;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.securityUtil = securityUtil;
    }

    //cart saver
    public Cart saveCart(User customer) {
        Cart cart = new Cart();
        cart.setUser(customer);
        return cartRepo.save(cart);
    }

    //add course to cart
    public Cart addCourseToCart(String courseRefAuthorId) {
        Optional<Course> course = courseRepo.findByCourseRefAuthorId(courseRefAuthorId);
        if (course.isPresent()) {
            Optional<User> customer = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
            if (customer.isPresent()) {
                List<MyCourse> myCourse = customer.get().getMyCourse();
                if (myCourse != null) {
                    Optional<MyCourse> myCourseOptional = myCourse.stream().filter(c -> c.getMyCourseRefAuthorId().equals(courseRefAuthorId)).findFirst();
                    if (myCourseOptional.isPresent()) {
                        throw new CourseAlreadyBoughtException("Course already bought");
                    }
                }

                Cart cart = customer.get().getCart();
                if (cart != null) {
                    List<Integer> existedCourseId = cart.getCourseId();
                    //if there is no course in cart create new list course id or else continue with existed course id list.
                    if (existedCourseId == null) {
                        List<Integer> courseIds = new ArrayList<>();
                        courseIds.add(course.get().getCourseId());
                        cart.setCourseId(courseIds);
                        cart.setCourseCount(courseIds.size());
                    } else {
                        List<Course> cartCourses = existedCourseId.stream().map(courseService::getCourse).toList();
                        Course coursePresentInCart = cartCourses.stream().filter(c -> c.getCourseId() == course.get().getCourseId()).findFirst().orElse(null);
                        if (coursePresentInCart == null) {
                            existedCourseId.add(course.get().getCourseId());
                            cart.setCourseId(existedCourseId);
                            cart.setCourseCount(existedCourseId.size());
                        } else throw new CourseAlreadyAddedInTheCartException("Course Already Present In Cart");
                    }
                    cart.setBillableAmount(cart.getBillableAmount() + course.get().getCoursePrice());
                    return cartRepo.save(cart);
                } else return null;
            } else throw new UserNotFoundException("User not found");
        } else throw new CourseNotFoundException("Course Not Found");
    }

    //remove course from cart
    public Cart removeCourseFromCart(String courseRefAuthorId) {
        Optional<Course> course = courseRepo.findByCourseRefAuthorId(courseRefAuthorId);
        if (course.isPresent()) {
            Optional<User> customer = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
            if (customer.isPresent()) {
                Cart cart = customer.get().getCart();
                if (cart != null) {
                    List<Integer> existedCourseId = cart.getCourseId();
                    if (!existedCourseId.isEmpty()) {
                        List<Integer> courseIds = existedCourseId.stream().filter(cId -> cId != course.get().getCourseId()).toList();
                        List<Integer> updatedCourseId = new ArrayList<>(courseIds);
                        cart.setCourseId(updatedCourseId);
                        cart.setCourseCount(courseIds.size());
                        cart.setBillableAmount(cart.getBillableAmount() - course.get().getCoursePrice());
                        return cartRepo.save(cart);
                    } else return null;
                } else return null;
            } else throw new UserNotFoundException("User not found");
        } else throw new CourseNotFoundException("Course Not Found");
    }

    //remove all course from cart
    public Cart removeAllCourse() {
        Optional<User> customer = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (customer.isPresent()) {
            Cart cart = customer.get().getCart();
            if (cart != null) {
                cart.setCourseId(null);
                cart.setCourseCount(0);
                cart.setBillableAmount(0);
                return cartRepo.save(cart);
            } else return null;
        } else throw new UserNotFoundException("User not found");
    }

    //cartDtoConverter
    public CartDto cartDtoConverter(Cart cart, List<Integer> courseId) {
        cartDto.setCourseCount(cart.getCourseCount());
        cartDto.setBillableAmount(cart.getBillableAmount());
        if (courseId != null) {
            List<Course> courses = courseId.stream().map(courseService::getCourse).toList();
            List<CustomerCourseDto> customerCourseDtoList = courses.stream().map(courseService::courseDtoConverterForCustomer).toList();
            cartDto.setCustomerCourseList(customerCourseDtoList);
        } else cartDto.setCustomerCourseList(null);
        return cartDto;
    }

}


