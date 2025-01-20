package com.graphode.journalapp.controller;

import com.graphode.journalapp.entity.User;
import com.graphode.journalapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin")
@Tag(name="Admin APIs")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Retrieve all users")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{id}")
    @Operation(summary = "Retrieve a user by their ID")
    public ResponseEntity<User> getById(@PathVariable String id) {
        ObjectId myID = new ObjectId(id);
        Optional<User> entry = userService.getById(myID);
        if (entry.isPresent()) {
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("create-admin")
    @Operation(summary = "Create a new admin user")
    public ResponseEntity<?> createAdmin(@RequestBody User newAdmin){
        try {
            userService.createAdministrator(newAdmin);
            return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}