package com.atoolz.htmx.todo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findAllByOrderByCreatedAtDesc();

  List<Todo> findByCompletedOrderByPriorityDescCreatedAtDesc(boolean completed);

  List<Todo> findByPriorityOrderByCreatedAtDesc(Todo.Priority priority);

  @Query("SELECT t FROM Todo t WHERE t.priority = :priority AND t.completed = :completed ORDER BY t.createdAt DESC")
  List<Todo> findByPriorityAndCompleted(Todo.Priority priority, boolean completed);
}
