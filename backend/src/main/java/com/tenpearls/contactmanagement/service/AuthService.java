

package com.tenpearls.contactmanagement.service;

import com.tenpearls.contactmanagement.dto.LoginRequest;
import com.tenpearls.contactmanagement.dto.RegisterRequest;
import com.tenpearls.contactmanagement.model.User;
import com.tenpearls.contactmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        String email = normalize(request.getEmail());
        String phone = normalize(request.getPhone());

        if (email == null && phone == null) {
            throw new IllegalArgumentException(
                    "Please provide either an email address or phone number."
            );
        }

        if (email != null && userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException(
                    "An account with this phone number already exists."
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(LoginRequest request) {

        String identifier = normalize(request.getIdentifier());

        User user = userRepository.findByEmailIgnoreCase(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid email/phone or password."
                ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "Invalid email/phone or password."
            );
        }

        return user;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
