package org.keni.tech.onlinecourseapplication.repo;

import org.keni.tech.onlinecourseapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email=?1 and u.password=?2")
    Optional<User> findUserByEmailAndPassword(String email, String password);
    @Modifying
    @Query("delete from User u where u.email=?1")
    Integer deleteUserByEmail(String email);
    @Query("select u from User u where u.email=?1")
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
