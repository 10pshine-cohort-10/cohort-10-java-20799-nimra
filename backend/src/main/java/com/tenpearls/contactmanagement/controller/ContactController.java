package com.tenpearls.contactmanagement.controller;

import com.tenpearls.contactmanagement.model.Contact;
import com.tenpearls.contactmanagement.repository.ContactRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {

    private final ContactRepository repo;
    public ContactController(ContactRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Contact> getAll() { return repo.findAll(); }

    @PostMapping
    public Contact create(@RequestBody Contact contact) {
    contact.setId(null);
    return repo.save(contact);
}
}