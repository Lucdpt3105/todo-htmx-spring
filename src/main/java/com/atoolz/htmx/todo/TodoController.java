package com.atoolz.htmx.todo;

import java.time.Instant;
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
  public String home(Model model) {
    model.addAttribute("todos", todos.findAllByOrderByCreatedAtDesc());
    return "home";
  }

  @PostMapping("/todos")
  @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
  public String create(
      @RequestParam(value = "title", required = false) String title, Model model) {
    if (title == null || title.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
    }
    Todo todo = new Todo(title.trim(), false, Instant.now());
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
