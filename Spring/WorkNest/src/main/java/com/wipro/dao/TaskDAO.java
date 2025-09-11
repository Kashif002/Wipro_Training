package com.wipro.dao;

import java.util.List;
import com.wipro.entities.Task;

public interface TaskDAO {
    Integer save(Task task);
    void update(Task task);
    void delete(Integer id);
    Task findById(Integer id);
    List<Task> findAll();
    List<Task> findByUser(Integer userId);
    List<Task> findByStatus(String status); // Add this method
    List<Task> findByAssignedBy(Integer adminId); // Add this method
}