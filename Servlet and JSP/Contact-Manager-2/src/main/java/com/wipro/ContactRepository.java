package com.wipro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContactRepository {
    private static List<Contact> contacts = new ArrayList<>();
    private static AtomicInteger idCounter = new AtomicInteger(1);

    public static List<Contact> getAll() {
        return new ArrayList<>(contacts); // Return a copy to prevent modification
    }

    public static boolean add(Contact contact) {
        if (contact.getName() == null || contact.getName().trim().isEmpty() ||
            contact.getEmail() == null || contact.getEmail().trim().isEmpty() ||
            contact.getPhone() == null || contact.getPhone().trim().isEmpty()) {
            return false; // Validation failed
        }
        contact.setId(idCounter.getAndIncrement());
        contacts.add(contact);
        return true;
    }

    public static Contact getById(int id) {
        return contacts.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static boolean update(Contact updatedContact) {
        if (updatedContact.getName() == null || updatedContact.getName().trim().isEmpty() ||
            updatedContact.getEmail() == null || updatedContact.getEmail().trim().isEmpty() ||
            updatedContact.getPhone() == null || updatedContact.getPhone().trim().isEmpty()) {
            return false; // Validation failed
        }
        Contact existing = getById(updatedContact.getId());
        if (existing != null) {
            existing.setName(updatedContact.getName());
            existing.setEmail(updatedContact.getEmail());
            existing.setPhone(updatedContact.getPhone());
            return true;
        }
        return false;
    }

    public static void delete(int id) {
        contacts.removeIf(c -> c.getId() == id);
    }
}