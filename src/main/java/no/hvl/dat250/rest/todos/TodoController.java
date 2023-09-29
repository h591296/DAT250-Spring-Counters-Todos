package no.hvl.dat250.rest.todos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest-Endpoint for todos.
 */
@RestController
public class TodoController {

  public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Todo with the id %s not found!";

  private List<Todo> todoList = new ArrayList<>();
  private Long nextId = 1L;

  // Create (POST)
  @PostMapping("/todos")
  public Todo createTodo(@RequestBody Todo todo) {
    todo.setId(nextId); // Assign a unique ID
    todoList.add(todo); // Add the Todo item to the in-memory storage
    return todo;
  }

  // Read (GET)
  @GetMapping("/todos/{todoId}")
  public ResponseEntity<?> getTodoById(@PathVariable Long todoId) {
    Optional<Todo> matchingTodo = todoList.stream().filter(todo -> todo.getId().equals(todoId)).findFirst();

    if (matchingTodo.isPresent()) {
      return ResponseEntity.ok(matchingTodo.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
    }
  }

  // Read (GET)
  @GetMapping("/todos")
  public List<Todo> getAllTodos() {
    return todoList;
  }

  // Update (PUT)
  @PutMapping("/todos/{todoId}")
  public ResponseEntity<?> updateTodo(@PathVariable Long todoId, @RequestBody Todo updatedTodo) {
    Optional<Todo> existingTodo = todoList.stream().filter(todo -> todo.getId().equals(todoId)).findFirst();

    if (existingTodo.isPresent()) {
      Todo todoToUpdate = existingTodo.get();
      todoToUpdate.setSummary(updatedTodo.getSummary());
      todoToUpdate.setDescription(updatedTodo.getDescription());

      int index = todoList.indexOf(todoToUpdate);
      if (index != -1) {
        todoList.set(index, todoToUpdate);
      }

      return ResponseEntity.ok(todoToUpdate);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
    }
  }

  // Delete (DELETE)
  @DeleteMapping("/todos/{todoId}")
  public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
    
    boolean removed = todoList.removeIf(todo -> todo.getId().equals(todoId));

    if (removed) {
      return ResponseEntity.ok("Todo item with ID " + todoId + " has been deleted.");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
    }
  }
}
