package com.Wallet.Application.module.security.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Wallet.Application.Dto.UserRequest;
import com.Wallet.Application.Entity.User;
import com.Wallet.Application.Repository.UserRepository;
import com.Wallet.Application.module.security.configuration.JwtService;
import com.Wallet.Application.module.security.exception.MasterException;
import com.Wallet.Application.module.security.role.Role;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthResponse register(UserRequest request) throws MasterException {
    var user= User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
    }

    public AuthResponse authenticate(AuthRequest request) throws MasterException{
    	String lowerCaseEmail = request.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		lowerCaseEmail,
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(lowerCaseEmail)
                .orElseThrow(() -> new MasterException("User not found"));

    // Pass the user object correctly to the generateToken method
    String jwtToken = jwtService.generateToken(user);  
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
    
    public String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " to get the token
        }
        return null; // Return null if the token is not found
    }
}
