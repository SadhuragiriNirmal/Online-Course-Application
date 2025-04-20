package org.keni.tech.onlinecourseapplication.repo;

import org.keni.tech.onlinecourseapplication.model.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyCourseRepo extends JpaRepository<MyCourse, Integer> {
}
