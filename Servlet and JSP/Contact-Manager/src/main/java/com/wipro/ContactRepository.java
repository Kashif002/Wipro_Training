//ContactRepository.java

package com.wipro;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
	private static List<Contact> contacts = new ArrayList<>();

	public static List<Contact> getAll() {
		return new ArrayList<>(contacts); // Return a copy to prevent modification
	}

	public static void add(Contact contact) {
		contacts.add(contact);
	}
}