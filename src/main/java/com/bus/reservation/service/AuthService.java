package com.bus.reservation.service;

import com.bus.reservation.model.User;
import com.bus.reservation.repository.UserRepository;
import com.bus.reservation.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public User registerUser(String name, String email, String phone, String password, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered!");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("Phone number already registered!");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode(password)) // Hash the password
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public Optional<Map<String, Object>> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", user.getRole().name());

                    String token = jwtUtils.generateToken(user.getEmail(), claims);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", user.getId());
                    response.put("name", user.getName());
                    response.put("role", user.getRole().name());

                    return response;
                });
    }
}
