package org.keni.tech.onlinecourseapplication.repo;

import org.keni.tech.onlinecourseapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
