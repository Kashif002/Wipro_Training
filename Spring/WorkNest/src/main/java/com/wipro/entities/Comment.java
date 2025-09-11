package com.wipro.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id") 
    private Task task;


    // Constructors
    public Comment() {}

    public Comment(String content, User user, Task task) {
        this.content = content;
        this.user = user;
        this.task = task;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", content=" + content + ", createdAt=" + createdAt + 
               ", user=" + (user != null ? user.getName() : "null") + 
               ", task=" + (task != null ? task.getTitle() : "null") + "]";
    }
}