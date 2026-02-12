package com.ems.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.AuthRequest;
import com.ems.dto.AuthResponse;
import com.ems.service.impl.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    
    private final JwtService jwt;

    @PostMapping("/login")
    public ResponseEntity<?> loginCheck(@RequestBody AuthRequest auth) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword());

        try {
            Authentication authentication = authManager.authenticate(token);

            if (authentication.isAuthenticated()) {
                // Get UserDetails from Authentication principal
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Generate token using UserDetails (with roles/authorities)
                String jwtToken = jwt.generateToken(userDetails);

                AuthResponse response = new AuthResponse();
                response.setEmail(userDetails.getUsername());
                response.setJwtToken(jwtToken);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
        }

        return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
    }
}
