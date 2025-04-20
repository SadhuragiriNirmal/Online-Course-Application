package org.keni.tech.onlinecourseapplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String firstName;
    private String lastName;
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Cart cart;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<MyCourse> myCourse;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Course> courses;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    public User(int userId, String firstName, String lastName, String email, Cart cart) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cart = cart;
    }

}
