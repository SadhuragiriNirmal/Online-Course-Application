package org.keni.tech.onlinecourseapplication.controller;

import org.keni.tech.onlinecourseapplication.dto.CartDto;
import org.keni.tech.onlinecourseapplication.model.Cart;
import org.keni.tech.onlinecourseapplication.service.CartService;
import org.keni.tech.onlinecourseapplication.util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
public class CartRestController {
    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping("load/course")
    public ResponseEntity<ResponseStructure<CartDto>> addCourseToCart(@RequestParam String courseRefAuthorId) {
        Cart cart = cartService.addCourseToCart(courseRefAuthorId);
        CartDto cartDto = cartService.cartDtoConverter(cart, cart.getCourseId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("Course Added Successfully In The Cart", HttpStatus.ACCEPTED.value(), cartDto));
    }

    @DeleteMapping("remove/course")
    public ResponseEntity<ResponseStructure<CartDto>> removeCourseFromCart(@RequestParam String courseRefAuthorId) {
        Cart updatedCart = cartService.removeCourseFromCart(courseRefAuthorId);
        CartDto updatedCartDto = cartService.cartDtoConverter(updatedCart, updatedCart.getCourseId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("Course Removed Successfully In The Cart", HttpStatus.ACCEPTED.value(), updatedCartDto));
    }

    @DeleteMapping("remove/courses")
    public ResponseEntity<ResponseStructure<CartDto>> makeCartEmpty() {
        Cart emptyCart = cartService.removeAllCourse();
        CartDto emptyCartDto = cartService.cartDtoConverter(emptyCart, null);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<>("All Courses Are Removed Successfully In the Cart", HttpStatus.ACCEPTED.value(), emptyCartDto));
    }
}

