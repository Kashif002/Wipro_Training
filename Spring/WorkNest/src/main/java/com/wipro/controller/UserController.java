package com.wipro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.wipro.entities.Task;
import com.wipro.entities.User;
import com.wipro.entities.Comment;
import com.wipro.services.TaskService;
import com.wipro.services.CommentService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private CommentService commentService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Task> tasks = taskService.listByUser(user.getId());
            model.addAttribute("tasks", tasks);
            model.addAttribute("user", user);
            model.addAttribute("role", "USER");
            return "userDashboard";
        }
        return "redirect:/login";
    }


    @GetMapping("/tasks/updateStatus")
    public String updateTaskStatus(@RequestParam Integer taskId, 
                                  @RequestParam String status,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task = taskService.get(taskId);
        
        // Check if the task belongs to the current user
        if (task != null && task.getAssignedTo().getId().equals(user.getId())) {
            task.setStatus(status);
            taskService.update(task);
        }
        return "redirect:/user/tasks";
    }

    @GetMapping("/tasks/comments")
    public String viewTaskComments(@RequestParam Integer taskId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task = taskService.get(taskId);
        
        // Check if the task belongs to the current user
        if (task != null && task.getAssignedTo().getId().equals(user.getId())) {
            List<Comment> comments = commentService.listByTask(taskId);
            model.addAttribute("task", task);
            model.addAttribute("comments", comments);
            return "taskComments";
        }
        return "redirect:/user/tasks";
    }

    @PostMapping("/tasks/comments/add")
    public String addComment(@RequestParam Integer taskId,
                            @RequestParam String content,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task = taskService.get(taskId);
        
        // Check if the task belongs to the current user
        if (task != null && task.getAssignedTo().getId().equals(user.getId())) {
            Comment comment = new Comment(content, user, task);
            commentService.add(comment);
        }
        return "redirect:/user/tasks/comments?taskId=" + taskId;
    }
    
    @GetMapping("/tasks")
    public String viewTasks(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Task> tasks = taskService.listByUser(user.getId());
            model.addAttribute("tasks", tasks);
            return "userTasks";
        }
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}