package com.wipro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{

}
