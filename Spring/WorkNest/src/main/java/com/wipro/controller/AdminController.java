package com.wipro.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wipro.entities.Comment;
import com.wipro.entities.Task;
import com.wipro.entities.User;
import com.wipro.services.CommentService;
import com.wipro.services.TaskService;
import com.wipro.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentService commentService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        
        // Get tasks by status for dashboard
        model.addAttribute("pendingTasks", taskService.listByStatus("PENDING"));
        model.addAttribute("inProgressTasks", taskService.listByStatus("IN_PROGRESS"));
        model.addAttribute("completedTasks", taskService.listByStatus("COMPLETED"));
        model.addAttribute("delayedTasks", taskService.listByStatus("DELAYED"));
        
        model.addAttribute("role", "ADMIN");
        return "adminDashboard";
    }

    @GetMapping("/tasks")
    public String manageTasks(Model model, HttpSession session) {
        User admin = (User) session.getAttribute("user");
        List<Task> tasks = taskService.listByAssignedBy(admin.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", userService.listAll());
        return "adminTasks";
    }

    @PostMapping("/tasks/add")
    public String addTask(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String status,
                         @RequestParam String startDate,
                         @RequestParam String dueDate,
                         @RequestParam Integer assignedTo,
                         HttpSession session) {
        User admin = (User) session.getAttribute("user");
        User user = userService.get(assignedTo);
        
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setStartDate(java.sql.Date.valueOf(startDate));
        task.setDueDate(java.sql.Date.valueOf(dueDate));
        task.setAssignedTo(user);
        task.setAssignedBy(admin);
        
        taskService.add(task);
        return "redirect:/admin/tasks";
    }

    @GetMapping("/tasks/updateStatus")
    public String updateTaskStatus(@RequestParam Integer taskId, 
                                  @RequestParam String status,
                                  HttpSession session) {
        User admin = (User) session.getAttribute("user");
        Task task = taskService.get(taskId);
        if (task != null) {
            task.setStatus(status);
            taskService.update(task);
        }
        return "redirect:/admin/tasks";
    }

    @GetMapping("/comments")
    public String manageComments(Model model) {
        List<Comment> comments = commentService.listAll();
        model.addAttribute("comments", comments);
        return "adminComments";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "adminUsers";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}