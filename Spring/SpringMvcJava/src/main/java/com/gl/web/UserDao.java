package com.gl.web;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveUser(User user) {
        try {
            String insertQuery = "insert into User (email, name, password) values (?, ?, ?)";
            int updatedRows = jdbcTemplate.update(insertQuery, user.getEmail(), user.getName(), user.getPassword());
            return updatedRows;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }

    public List<User> getUsers() {
        try {
            String selectQuery = "select * from User";
            return jdbcTemplate.query(selectQuery, new RowMapperImpl());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve users: " + e.getMessage());
        }
    }
}