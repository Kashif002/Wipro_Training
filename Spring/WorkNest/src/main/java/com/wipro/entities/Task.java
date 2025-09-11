package com.wipro.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED, DELAYED

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id") 
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assigned_by_id") 
    private User assignedBy;

    // Constructors
    public Task() {}

    public Task(String title, String description, String status, Date startDate, Date dueDate, User assignedTo, User assignedBy) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
    
    public User getAssignedBy() { return assignedBy; }
    public void setAssignedBy(User assignedBy) { this.assignedBy = assignedBy; }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", description=" + description + 
               ", status=" + status + ", startDate=" + startDate + ", dueDate=" + dueDate + 
               ", assignedTo=" + (assignedTo != null ? assignedTo.getName() : "null") + 
               ", assignedBy=" + (assignedBy != null ? assignedBy.getName() : "null") + "]";
    }
}