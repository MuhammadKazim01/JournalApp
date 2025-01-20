package com.graphode.journalapp.controller;

import com.graphode.journalapp.service.UserDetailsServiceImpl;
import com.graphode.journalapp.service.UserService;
import com.graphode.journalapp.utils.jwtUtil;
import com.graphode.journalapp.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/")
@Tag(name="Public APIs")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private jwtUtil jwtUtil;

    @PostMapping("sign-up")
    @Operation(summary = "Create a new user account")
    public ResponseEntity<User> signUp(@RequestBody User newEntry) {
        try {
            userService.createNewUseAndUpdate(newEntry);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating user: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("login")
    @Operation(summary = "Login to the system and obtain a JWT token")
    public ResponseEntity<String> login(@RequestBody User credentials) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Invalid credentials: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("health-check")
    @Operation(summary = "Check the health status of the application")
    public String healthCheck() {
        return "OK";
    }
}