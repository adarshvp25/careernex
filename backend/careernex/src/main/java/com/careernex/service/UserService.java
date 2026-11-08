
package com.careernex.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.careernex.entity.User;
import com.careernex.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String fullName, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered!");
        }

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
    

    public User loginUser(String email, String password) {

    	User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("Invalid email or password"));

    	if (!user.getEnabled()) {
    		throw new RuntimeException("Account is disabled");
    	}

    	if (!passwordEncoder.matches(password, user.getPasswordHash())) {
    		throw new RuntimeException("Invalid email or password");
    	}

    	return user;
    }

}
