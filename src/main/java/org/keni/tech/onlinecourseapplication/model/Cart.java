package org.keni.tech.onlinecourseapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int courseCount;
    private double billableAmount;
    @OneToOne
    private User user;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "courseId")
    private List<Integer> courseId;

    public Cart() {
    }

    public Cart(int cartId, int courseCount, double billableAmount, User user, List<Integer> courseId) {
        this.cartId = cartId;
        this.courseCount = courseCount;
        this.billableAmount = billableAmount;
        this.user = user;
        this.courseId = courseId;
    }

}