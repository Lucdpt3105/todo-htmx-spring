package com.atoolz.htmx.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "todos")
public class Todo {

  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String title;

  @Column(nullable = false)
  private boolean completed;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Priority priority = Priority.MEDIUM;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected Todo() {}

  public Todo(String title, boolean completed, Instant createdAt, String userId) {
    this.title = title;
    this.completed = completed;
    this.createdAt = createdAt;
    this.priority = Priority.MEDIUM;
    this.userId = userId;
  }

  public Todo(String title, boolean completed, Priority priority, LocalDate dueDate, Instant createdAt, String userId) {
    this.title = title;
    this.completed = completed;
    this.priority = priority;
    this.dueDate = dueDate;
    this.createdAt = createdAt;
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
