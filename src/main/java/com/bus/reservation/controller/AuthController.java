package com.bus.reservation.controller;

import com.bus.reservation.model.User;
import com.bus.reservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String phone = request.get("phone");
        String password = request.get("password");
        String role = request.get("role"); // "ADMIN" or "CUSTOMER"

        User newUser = authService.registerUser(
                name, email, phone, password, User.Role.valueOf(role.toUpperCase())
        );
        return ResponseEntity.ok(newUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        return authService.loginUser(email, password)
        		.map(ResponseEntity::ok)
        		.orElseGet(() -> ResponseEntity.status(401)
        	            .body(Map.of("error", "Invalid credentials")));
    }
}
