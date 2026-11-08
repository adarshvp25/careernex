
package com.careernex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.careernex.entity.User;
import com.careernex.service.UserService;

import com.careernex.security.JwtService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {

        User user = userService.registerUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                "User registered successfully! User ID: " + user.getId()
        );
    }
    

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
	
	    User user = userService.loginUser(
	            request.getEmail(),
	            request.getPassword()
	    );
	
	    String token = jwtService.generateToken(user.getEmail());
	
	    return ResponseEntity.ok(Map.of(
	            "token", token,
	            "tokenType", "Bearer"
	    ));
	}


    public static class RegisterRequest {

        private String fullName;
        private String email;
        private String password;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
