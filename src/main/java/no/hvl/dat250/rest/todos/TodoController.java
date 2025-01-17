package no.hvl.dat250.rest.todos;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest-Endpoint for todos.
 */
@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "http://localhost:4200")
public class TodoController {

  public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Todo with the id %s not found!";

  private List<Todo> todoList = new ArrayList<>();
  private Long nextId = 1L;

  // Create (POST)
  @PostMapping("/add")
  public Todo createTodo(@RequestBody Todo todo) {
    todo.setId(nextId);
    todoList.add(todo);
    nextId++;
    return todo;
  }

  // Read (GET)
  @GetMapping("/{todoId}")
  public Todo getTodoById(@PathVariable Long todoId) {
    for (Todo todo : todoList){
      if (todo.getId().equals(todoId)) {
        return todo;
      }
    }
    throw new NoSuchElementException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
  }

  // Read (GET)
  @GetMapping
  public List<Todo> getTodos() {
    return todoList;
  }

  // Update (PUT)
  @PutMapping("/{todoId}")
  public Todo updateTodo(@PathVariable Long todoId, @RequestBody Todo updatedTodo) {
    
    for (Todo todo : todoList) {
      if (todo.getId().equals(todoId)) {
        todo.setSummary(updatedTodo.getSummary());
        todo.setDescription(updatedTodo.getDescription());
        return todo;
      }
    }
    throw new NoSuchElementException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
  }

  // Delete (DELETE)
  @DeleteMapping("/{todoId}")
  public Todo deleteTodo(@PathVariable Long todoId) {

    for (Todo todo : todoList) {
      if (todo.getId().equals(todoId)) {
        todoList.remove(todo);
        return todo;
      }
    }
    throw new NoSuchElementException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, todoId));
  }
}