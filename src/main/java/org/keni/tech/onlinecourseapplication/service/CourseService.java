package org.keni.tech.onlinecourseapplication.service;

import org.keni.tech.onlinecourseapplication.dto.CourseDto;
import org.keni.tech.onlinecourseapplication.dto.CustomerCourseDto;
import org.keni.tech.onlinecourseapplication.exception.CourseNotFoundException;
import org.keni.tech.onlinecourseapplication.exception.InvalidEmailException;
import org.keni.tech.onlinecourseapplication.exception.UserNotFoundException;
import org.keni.tech.onlinecourseapplication.model.Cart;
import org.keni.tech.onlinecourseapplication.model.Course;
import org.keni.tech.onlinecourseapplication.model.User;
import org.keni.tech.onlinecourseapplication.repo.CartRepo;
import org.keni.tech.onlinecourseapplication.repo.CourseRepo;
import org.keni.tech.onlinecourseapplication.repo.UserRepo;
import org.keni.tech.onlinecourseapplication.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;
    private final SecurityUtil securityUtil;
    private final CartRepo cartRepo;

    public CourseService(CourseRepo courseRepo, UserRepo userRepo, SecurityUtil securityUtil, CartRepo cartRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.securityUtil = securityUtil;
        this.cartRepo = cartRepo;
    }

    //add course
    public Course addCourse(Course course) {
        Optional<User> author = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (author.isPresent()) {
            Course savedCourse = courseRepo.save(course);
            String courseRefAuthorId = author.get().getFirstName().substring(0, 2) + author.get().getLastName().substring(0, 2) + savedCourse.getCourseId();
            course.setCourseRefAuthorId(courseRefAuthorId);
            course.setAuthorName(author.get().getFirstName());
            Course updatedSavedCourse = courseRepo.save(course);
            List<Course> courses = author.get().getCourses();
            if (courses == null) {
                List<Course> newCourses = new ArrayList<>();
                newCourses.add(updatedSavedCourse);
                author.get().setCourses(newCourses);
            } else {
                courses.add(updatedSavedCourse);
                author.get().setCourses(courses);
            }
            userRepo.save(author.get());
            return updatedSavedCourse;
        } else throw new InvalidEmailException("Invalid email id");
    }

    //get course (Author)
    public Course getCourse(String courseRefAuthorId) {
        Optional<User> author = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (author.isPresent()) {
            List<Course> authorAssociatedCourse = author.get().getCourses();
            Course course = authorAssociatedCourse.stream().filter(c -> c.getCourseRefAuthorId().equals(courseRefAuthorId)).findFirst().orElse(null);
            if (course != null) {
                if (course.getCourseRefAuthorId().equals(courseRefAuthorId)) {
                    return course;
                } else return null;
            } else throw new CourseNotFoundException("Course not found");
        } else throw new UserNotFoundException("user not found");
    }

    public List<Course> getAllCourse() {
        Optional<User> author = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (author.isPresent()) {
            List<Course> courses = author.get().getCourses();
            if (!courses.isEmpty()) return courses;
            else throw new CourseNotFoundException("Course not found");
        } else throw new InvalidEmailException("Invalid email id");
    }

    //update course
    public Course updateCourse(Course course, String courseRefAuthorId) {
        Optional<User> author = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (author.isPresent()) {
            List<Course> authorAssociatedCourse = author.get().getCourses();
            Course existCourse = authorAssociatedCourse.stream().filter(c -> c.getCourseRefAuthorId().equals(courseRefAuthorId)).findFirst().orElse(null);
            if (existCourse != null) {
                if (existCourse.getCourseRefAuthorId().equals(courseRefAuthorId)) {
                    course.setCourseId(existCourse.getCourseId());
                    course.setCourseRefAuthorId(courseRefAuthorId);
                    course.setAuthorName(existCourse.getAuthorName());
                    return courseRepo.save(course);
                } else return null;
            } else throw new CourseNotFoundException("Course not found");
        } else throw new UserNotFoundException("user not found");
    }

    //delete course
    public Course deleteCourse(String courseRefAuthorId) {
        Optional<User> author = userRepo.findByEmail(securityUtil.getAuthenticatedUserEmail());
        if (author.isPresent()) {
            List<Course> authorAssociatedCourse = author.get().getCourses();
            Course existCourse = authorAssociatedCourse.stream().filter(c -> c.getCourseRefAuthorId().equals(courseRefAuthorId)).findFirst().orElse(null);
            if (existCourse != null) {
                if (existCourse.getCourseRefAuthorId().equals(courseRefAuthorId)) {
                    cartRefresher(existCourse);
                    List<Course> updatedCourse = authorAssociatedCourse.stream().filter(c -> !c.getCourseRefAuthorId().equals(courseRefAuthorId)).toList();
                    List<Course> newCourses = new ArrayList<>(updatedCourse);
                    author.get().setCourses(newCourses);
                    userRepo.save(author.get());
                    courseRepo.delete(existCourse);
                    return existCourse;
                } else return null;
            } else throw new CourseNotFoundException("Course not found");
        } else throw new UserNotFoundException("user not found");
    }

    public Course getCourse(Integer courseId) {
        Optional<Course> existCourse = courseRepo.findById(courseId);
        if (existCourse.isPresent()) {
            return existCourse.get();
        } else throw new CourseNotFoundException("Course not found");
    }

    public List<Course> getAllCourseForCustomer() {
        List<Course> courseList = courseRepo.findAll();
        if (!courseList.isEmpty()) return courseList;
        else throw new CourseNotFoundException("Course not found");
    }

    public void cartRefresher(Course course) {
        List<Cart> cartList = cartRepo.findAll();
        for (Cart cart : cartList) {
            if (!cart.getCourseId().isEmpty()) {
                List<Integer> courseIdList = cart.getCourseId().stream().filter(cId -> cId != course.getCourseId()).toList();
                List<Integer> updatedCourseId = new ArrayList<>(courseIdList);
                cart.setCourseId(updatedCourseId);
                cart.setCourseCount(updatedCourseId.size());
                cart.setBillableAmount(cart.getBillableAmount() - course.getCoursePrice());
                cartRepo.save(cart);
            }
        }
    }

    public CourseDto courseDtoConverter(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName(course.getCourseName());
        courseDto.setCourseRefAuthorId(course.getCourseRefAuthorId());
        courseDto.setCourseDescription(course.getCourseDescription());
        courseDto.setCourseObjective(course.getCourseObjective());
        courseDto.setAuthorName(course.getAuthorName());
        courseDto.setCourseDemoVideo(course.getCourseDemoVideo());
        courseDto.setCourseVideoResources(course.getCourseVideoResources());
        courseDto.setCoursePrice(course.getCoursePrice());
        return courseDto;
    }

    public CustomerCourseDto courseDtoConverterForCustomer(Course course) {
        CustomerCourseDto customerCourseDto = new CustomerCourseDto();
        customerCourseDto.setCourseName(course.getCourseName());
        customerCourseDto.setCourseRefAuthorId(course.getCourseRefAuthorId());
        customerCourseDto.setCourseDescription(course.getCourseDescription());
        customerCourseDto.setCourseObjective(course.getCourseObjective());
        customerCourseDto.setAuthorName(course.getAuthorName());
        customerCourseDto.setCourseDemoVideo(course.getCourseDemoVideo());
        customerCourseDto.setCoursePrice(course.getCoursePrice());
        return customerCourseDto;
    }
}