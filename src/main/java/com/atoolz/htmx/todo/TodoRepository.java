package com.atoolz.htmx.todo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findByUserIdOrderByCreatedAtDesc(String userId);

  List<Todo> findByUserIdAndCompletedOrderByPriorityDescCreatedAtDesc(String userId, boolean completed);

  List<Todo> findByUserIdAndPriorityOrderByCreatedAtDesc(String userId, Todo.Priority priority);

  @Query("SELECT t FROM Todo t WHERE t.userId = :userId AND t.priority = :priority AND t.completed = :completed ORDER BY t.createdAt DESC")
  List<Todo> findByUserIdAndPriorityAndCompleted(String userId, Todo.Priority priority, boolean completed);

  Optional<Todo> findByIdAndUserId(Long id, String userId);
}
