package org.keni.tech.onlinecourseapplication.service;

import lombok.AllArgsConstructor;
import org.keni.tech.onlinecourseapplication.dto.MyCourseDto;
import org.keni.tech.onlinecourseapplication.model.Cart;
import org.keni.tech.onlinecourseapplication.model.Course;
import org.keni.tech.onlinecourseapplication.model.MyCourse;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.repo.CartRepo;
import org.keni.tech.onlinecourseapplication.repo.MyCourseRepo;
import org.keni.tech.onlinecourseapplication.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MyCourseService {

    private final MyCourseRepo myCourseRepo;
    private final MyCourseDto myCourseDto;
    private final UserRepo userRepo;
    private final CourseService courseService;
    private final CartRepo cartRepo;

    public MyCourseDto myCourseDtoConverter(MyCourse myCourse) {
        myCourseDto.setMyCourseName(myCourse.getMyCourseName());
        myCourseDto.setMyCourseDescription(myCourse.getMyCourseDescription());
        myCourseDto.setMyCourseObjective(myCourse.getMyCourseObjective());
        myCourseDto.setMyCourseVideoResources(myCourse.getMyCourseVideoResources());
        return myCourseDto;
    }

    public MyCourse MyCourseSaver(Course course) {
        MyCourse myCourse = new MyCourse();
        myCourse.setMyCourseName(course.getCourseName());
        myCourse.setMyCourseDescription(course.getCourseDescription());
        myCourse.setMyCourseObjective(course.getCourseObjective());
        myCourse.setMyCourseVideoResources(course.getCourseVideoResources());
        myCourse.setMyCourseRefAuthorId(course.getCourseRefAuthorId());
        return myCourseRepo.save(myCourse);
    }

    public void myCourseConverter(User customer) {
        List<Course> courses = customer.getCart().getCourseId().stream()
                .map(courseService::getCourse).toList();
        List<MyCourse> myCourses = courses.stream().map(this::MyCourseSaver).toList();
        Cart cart = customer.getCart();
        List<MyCourse> newMyCourses = new ArrayList<>(myCourses);
        cart.setCourseCount(0);
        cart.setCourseId(null);
        cart.setBillableAmount(0);
        Cart savedCart = cartRepo.save(cart);
        customer.setCart(savedCart);
        customer.setMyCourse(newMyCourses);
        userRepo.save(customer);
    }

}
