package com.wipro.dao;

import java.util.List;
import com.wipro.entities.Comment;

public interface CommentDAO {
    Integer save(Comment comment);
    void update(Comment comment);
    void delete(Integer id);
    Comment findById(Integer id);
    List<Comment> findAll();
    List<Comment> findByTask(Integer taskId);
    List<Comment> findByUser(Integer userId);
}