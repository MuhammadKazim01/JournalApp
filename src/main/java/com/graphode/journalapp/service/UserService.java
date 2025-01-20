package com.graphode.journalapp.service;

import com.graphode.journalapp.entity.User;
import com.graphode.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> getById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void createNewUseAndUpdate(User userEntry) {
        userEntry.setPassword(encoder.encode(userEntry.getPassword()));
        if (userEntry.getRoles() == null || userEntry.getRoles().isEmpty()) {
            userEntry.setRoles(Arrays.asList("USER"));
        }
        userRepo.save(userEntry);
    }
    
    public void createAdministrator(User userEntry) {
        userEntry.setPassword(encoder.encode(userEntry.getPassword()));
        if (userEntry.getRoles() == null || userEntry.getRoles().isEmpty()) {
            userEntry.setRoles(Arrays.asList("USER", "ADMIN"));
        }
        userRepo.save(userEntry);
    }

    public void saveEntry(User userEntry) {
        userRepo.save(userEntry);
    }

    public Optional<User> getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void deleteById(ObjectId id) {
        userRepo.deleteById(id);
    }
}
