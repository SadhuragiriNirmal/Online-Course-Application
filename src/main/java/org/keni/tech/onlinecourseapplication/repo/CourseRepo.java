package org.keni.tech.onlinecourseapplication.repo;

import org.keni.tech.onlinecourseapplication.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Integer> {
}
