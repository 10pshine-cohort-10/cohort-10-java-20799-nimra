
package com.tenpearls.contactmanagement.controller;

import com.tenpearls.contactmanagement.dto.RegisterRequest;
import com.tenpearls.contactmanagement.model.User;
import com.tenpearls.contactmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tenpearls.contactmanagement.dto.LoginRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Registration successful");
        response.put("id", user.getId());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("createdAt", user.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(
        @Valid @RequestBody LoginRequest request) {

    User user = authService.login(request);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("message", "Login successful");
    response.put("id", user.getId());
    response.put("firstName", user.getFirstName());
    response.put("lastName", user.getLastName());
    response.put("email", user.getEmail());
    response.put("phone", user.getPhone());

    return ResponseEntity.ok(response);
}
}

