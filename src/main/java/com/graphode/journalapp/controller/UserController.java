package com.graphode.journalapp.controller;

import com.graphode.journalapp.entity.User;
import com.graphode.journalapp.service.UserService;
import com.graphode.journalapp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("user")
@Tag(name="User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    @Operation(summary = "Update user information")
    public ResponseEntity<?> updateUserEntry(@RequestBody User updateEntry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> oldEntryOpt = userService.getByUsername(username);
        if (oldEntryOpt.isPresent()) {
            User oldEntry = oldEntryOpt.get();
            oldEntry.setPassword(updateEntry.getPassword());
            userService.createNewUseAndUpdate(oldEntry);
            return ResponseEntity.ok("User updated successfully");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get personalized greeting with weather information")
    public ResponseEntity<?> greeting() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> oldEntryOpt = userService.getByUsername(username);

        if (oldEntryOpt.isPresent()) {
            String feelsLike = String.valueOf(weatherService.getWeather("Karachi").getCurrent().getFeelsLike());
            return new ResponseEntity<>("Hi " + username + ", Weather feels like " + feelsLike, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
