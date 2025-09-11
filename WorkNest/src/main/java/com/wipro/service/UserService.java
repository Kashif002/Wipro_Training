package com.wipro.service;

import com.wipro.dto.UserDto;
import com.wipro.entity.User;
import com.wipro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getIsActive()))
                .collect(Collectors.toList());
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User findUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    // Enhanced CRUD operations
    
    /**
     * Create a new user (for admin)
     */
    public User createUser(User user) {
        // Validate email uniqueness
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        return save(user);
    }
    
    /**
     * Update user details (without password)
     */
    public User updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            // Don't update password here - use separate method
            return userRepository.save(existingUser);
        }
        return null;
    }
    
    /**
     * Update user password
     */
    public User updateUserPassword(Integer id, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }
        return null;
    }
    
    /**
     * Delete a user
     */
    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Find users by role
     */
    public List<User> findUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Find team members for a user
     */
    public List<User> findTeamMembersForUser(Integer userId) {
        return userRepository.findTeamMatesForUser(userId);
    }
    
    /**
     * Find users in a specific group
     */
    public List<User> findUsersByGroupId(Integer groupId) {
        return userRepository.findByGroupId(groupId);
    }
    
    /**
     * Search users by name
     */
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Get user statistics
     */
    public Map<String, Long> getUserStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) userRepository.findAll().size());
        stats.put("admins", userRepository.countByRole("ADMIN"));
        stats.put("users", userRepository.countByRole("USER"));
        return stats;
    }
}
