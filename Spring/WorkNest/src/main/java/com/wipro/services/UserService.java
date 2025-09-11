package com.wipro.services;

import java.util.List;
import com.wipro.entities.User;

public interface UserService {
    Integer register(User user);
    void update(User user);
    void delete(Integer id);
    User get(Integer id);
    User getByEmail(String email);
    User getByName(String name); // Added for username lookup
    List<User> listAll();
}