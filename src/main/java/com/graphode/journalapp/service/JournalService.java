package com.graphode.journalapp.service;

import com.graphode.journalapp.entity.JournalEntry;
import com.graphode.journalapp.entity.User;
import com.graphode.journalapp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JournalService {
    @Autowired
    private JournalRepository journalrepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean saveEntry(JournalEntry journalentry, String username) {
        try {
            Optional<User> user = userService.getByUsername(username);
            if (user.isPresent()) {
                User retrievedUser = user.get();
                journalentry.setDate(LocalDateTime.now());
                JournalEntry newEntry = journalrepository.save(journalentry);
                retrievedUser.getJournalEntries().add(newEntry);
                userService.saveEntry(retrievedUser);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occured while saving entry", e);
        }
    }

    public void saveEntry(JournalEntry journalentry) {
        journalrepository.save(journalentry);
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalrepository.findById(id);
    }

    @Transactional
    public boolean deleteById(String username, ObjectId id) {
        Optional<User> user = userService.getByUsername(username);
        if (user.isPresent()) {
            User retrievedUser = user.get();
            retrievedUser.getJournalEntries().removeIf(x -> x.getId().equals(id));
            userService.saveEntry(retrievedUser);
            journalrepository.deleteById(id);
            return true;
        }
        return false;
    }
}
