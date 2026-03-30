package com.atoolz.htmx.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "todos")
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String title;

  @Column(nullable = false)
  private boolean completed;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected Todo() {}

  public Todo(String title, boolean completed, Instant createdAt) {
    this.title = title;
    this.completed = completed;
    this.createdAt = createdAt;
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

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }
}
