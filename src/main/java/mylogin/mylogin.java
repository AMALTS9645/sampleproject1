 ```java
// code-start
package com.example.demo.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Security Practice: Use a password encoder for handling passwords securely
    private final PasswordEncoder passwordEncoder;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String encodedPassword = passwordEncoder.encode(loginRequest.getPassword());

        Optional<User> user = userService.findByUsername(loginRequest.getUsername());

        if (user.isPresent()) {
            if (passwordMatches(encodedPassword, user.get().getPassword())) {
                // Security Practice: Generate a secure token for session management
                String token = tokenService.generateToken(user.get().getId());
                return responseWithToken(token);
            }
        }

        return "Login failed";
    }

    private boolean passwordMatches(String encodedPassword, String userPassword) {
        return passwordEncoder.matches(encodedPassword, userPassword);
    }

    // Extracted method to generate a response with a token
    private String responseWithToken(String token) {
        return "Login successful. Token: " + token;
    }

    // Other methods and classes for user and token services not shown
}
// code-end

// Other classes and methods omitted for brevity
```