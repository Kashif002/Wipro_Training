package com.wipro.services;

import java.util.List;
import com.wipro.entities.Comment;

public interface CommentService {
    Integer add(Comment comment);
    void update(Comment comment);
    void delete(Integer id);
    Comment get(Integer id);
    List<Comment> listAll();
    List<Comment> listByTask(Integer taskId);
    List<Comment> listByUser(Integer userId);
}