package org.keni.tech.onlinecourseapplication.controller;

import org.keni.tech.onlinecourseapplication.dto.CourseDto;
import org.keni.tech.onlinecourseapplication.dto.CustomerCourseDto;
import org.keni.tech.onlinecourseapplication.model.Course;
import org.keni.tech.onlinecourseapplication.service.CourseService;
import org.keni.tech.onlinecourseapplication.util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("course")
public class CourseRestController {
    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    //author operation

    @PostMapping("author/entry")
    public ResponseEntity<ResponseStructure<CourseDto>> addCourse(@RequestBody Course course) {
        Course savedCourse = courseService.addCourse(course);
        CourseDto courseDto = courseService.courseDtoConverter(savedCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<>("Course Upload Successfully", HttpStatus.CREATED.value(), courseDto));
    }

    @GetMapping("author/course")
    public ResponseEntity<ResponseStructure<CourseDto>> getCourse(@RequestParam String courseRefAuthorId) {
        Course course = courseService.getCourse(courseRefAuthorId);
        CourseDto courseDto = courseService.courseDtoConverter(course);
        return ResponseEntity.ok().body(new ResponseStructure<>("Course Found", HttpStatus.FOUND.value(), courseDto));
    }

    @GetMapping("author/course/pool")
    public ResponseEntity<ResponseStructure<List<CourseDto>>> getCoursePool() {
        List<Course> courseList = courseService.getAllCourse();
        List<CourseDto> courseDtoList = courseList.stream().map(courseService::courseDtoConverter).toList();
        return ResponseEntity.ok().body(new ResponseStructure<>("Courses Found", HttpStatus.FOUND.value(), courseDtoList));
    }

    @PutMapping("author/update/course")
    public ResponseEntity<ResponseStructure<CourseDto>> modifyCourse(@RequestBody Course course, @RequestParam String courseRefAuthorId) {
        Course updatedCourse = courseService.updateCourse(course, courseRefAuthorId);
        CourseDto courseDto = courseService.courseDtoConverter(updatedCourse);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("Course Updated Successfully", HttpStatus.ACCEPTED.value(), courseDto));
    }

    @DeleteMapping("author/delete/remover")
    public ResponseEntity<ResponseStructure<CourseDto>> deleteCourse(@RequestParam String courseRefAuthorId) {
        Course deletedCourse = courseService.deleteCourse(courseRefAuthorId);
        CourseDto courseDto = courseService.courseDtoConverter(deletedCourse);
        return ResponseEntity.ok().body(new ResponseStructure<>("Course Deleted Successfully", HttpStatus.OK.value(), courseDto));
    }

    //customer operation
    @GetMapping("customer/course/pool")
    public ResponseEntity<ResponseStructure<List<CustomerCourseDto>>> getAllCourseForCustomer() {
        List<Course> courseList = courseService.getAllCourseForCustomer();
        List<CustomerCourseDto> courseDtoList = courseList.stream().map(courseService::courseDtoConverterForCustomer).toList();
        return ResponseEntity.ok().body (new ResponseStructure<>("Courses Found", HttpStatus.OK.value(), courseDtoList));
    }

}