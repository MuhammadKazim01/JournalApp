package com.graphode.journalapp.controller;

import com.graphode.journalapp.entity.JournalEntry;
import com.graphode.journalapp.entity.User;
import com.graphode.journalapp.service.JournalService;
import com.graphode.journalapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("journal")
@Tag(name="Journal APIs")
public class JournalEntryController {

    @Autowired
    private JournalService journalservice;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Retrieve all journal entries for the authenticated user")
    public ResponseEntity<List<JournalEntry>> getAllJournal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.getByUsername(username);
        if (user.isPresent()) {
            User retrievedUser = user.get();
            List<JournalEntry> entries = retrievedUser.getJournalEntries();
            if (entries != null && !entries.isEmpty()) {
                return new ResponseEntity<>(entries, HttpStatus.OK);
            }
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myid}")
    @Operation(summary = "Retrieve a specific journal entry by its ID")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable String myid) {
        ObjectId id = new ObjectId(myid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.getByUsername(username);
        if (user.isPresent()) {
            User retrievedUser = user.get();
            Optional<JournalEntry> entry = retrievedUser.getJournalEntries().stream()
                    .filter(journalEntry -> journalEntry.getId().equals(id))
                    .findFirst();
            if (entry.isPresent()) {
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @Operation(summary = "Create a new journal entry for the authenticated user")
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry newEntry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            boolean result = journalservice.saveEntry(newEntry, username);
            if (result) {
                return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{myid}")
    @Operation(summary = "Delete a journal entry by its ID")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String myid) {
        ObjectId id = new ObjectId(myid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.getByUsername(username);
        User retrievedUser = user.get();
        Optional<JournalEntry> collect = retrievedUser.getJournalEntries().stream()
                .filter(journalEntry -> journalEntry.getId().equals(id))
                .findFirst();
        if(collect != null){
            boolean result = journalservice.deleteById(username, id);
            if (result) {
                return ResponseEntity.ok("Journal deleted successfully");
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myid}")
    @Operation(summary = "Update a journal entry by its ID")
    public ResponseEntity<JournalEntry> updateJournalEntry(@RequestBody JournalEntry updateEntry, @PathVariable String myid) {
        ObjectId id = new ObjectId(myid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userService.getByUsername(username);
        if (user.isPresent()) {
            Optional<JournalEntry> oldEntryOpt = journalservice.getById(id);
            if (oldEntryOpt.isPresent()) {
                JournalEntry oldEntry = oldEntryOpt.get();
                oldEntry.setTitle(updateEntry.getTitle() != null && !updateEntry.getTitle().equals("") ? updateEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(updateEntry.getContent() != null && !updateEntry.getContent().equals("") ? updateEntry.getContent() : oldEntry.getContent());
                journalservice.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}