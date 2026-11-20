package com.tenpearls.contactmanagement.repository;

import com.tenpearls.contactmanagement.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {}