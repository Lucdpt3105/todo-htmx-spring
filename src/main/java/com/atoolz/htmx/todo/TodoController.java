package com.atoolz.htmx.todo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TodoController {

  private final TodoRepository todos;

  public TodoController(TodoRepository todos) {
    this.todos = todos;
  }

  @GetMapping("/")
  public String home(
      @RequestParam(value = "filter", required = false, defaultValue = "all") String filter,
      @RequestParam(value = "priority", required = false) String priority,
      Model model) {
    List<Todo> todoList;

    if ("completed".equals(filter)) {
      todoList = todos.findByCompletedOrderByPriorityDescCreatedAtDesc(true);
    } else if ("active".equals(filter)) {
      todoList = todos.findByCompletedOrderByPriorityDescCreatedAtDesc(false);
    } else if (priority != null && !priority.isBlank()) {
      todoList = todos.findByPriorityOrderByCreatedAtDesc(Todo.Priority.valueOf(priority.toUpperCase()));
    } else {
      todoList = todos.findAllByOrderByCreatedAtDesc();
    }

    model.addAttribute("todos", todoList);
    model.addAttribute("currentFilter", filter);
    model.addAttribute("currentPriority", priority);
    return "home";
  }

  @PostMapping("/todos")
  @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
  public String create(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "priority", required = false, defaultValue = "MEDIUM") String priority,
      @RequestParam(value = "dueDate", required = false) String dueDate,
      Model model) {
    if (title == null || title.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
    }

    LocalDate due = null;
    if (dueDate != null && !dueDate.isBlank()) {
      try {
        due = LocalDate.parse(dueDate);
      } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid dueDate format");
      }
    }

    Todo todo = new Todo(
        title.trim(),
        false,
        Todo.Priority.valueOf(priority.toUpperCase()),
        due,
        Instant.now()
    );
    todos.save(todo);
    model.addAttribute("todo", todo);
    return "fragments/todo-item :: todoRow";
  }

  @PatchMapping("/todos/{id}/toggle")
  public String toggle(@PathVariable("id") long id, Model model) {
    Todo todo =
        todos
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    todo.setCompleted(!todo.isCompleted());
    todos.save(todo);
    model.addAttribute("todo", todo);
    return "fragments/todo-item :: todoRow";
  }

  @DeleteMapping("/todos/{id}")
  @ResponseBody
  public ResponseEntity<Void> delete(@PathVariable("id") long id) {
    todos.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/health")
  @ResponseBody
  public ResponseEntity<Map<String, String>> health() {
    try {
      todos.count();
      return ResponseEntity.ok(Map.of("status", "healthy"));
    } catch (Exception e) {
      return ResponseEntity.status(503).body(Map.of("status", "unhealthy", "error", e.getMessage()));
    }
  }
}
