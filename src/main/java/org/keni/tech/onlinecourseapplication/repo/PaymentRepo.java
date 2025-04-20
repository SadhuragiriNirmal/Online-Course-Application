package org.keni.tech.onlinecourseapplication.repo;

import org.keni.tech.onlinecourseapplication.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}
