package com.taskhub.controller;

import com.taskhub.dao.ToDoDAO;
import com.taskhub.dao.ToDoDAOImpl;
import com.taskhub.model.ToDoItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    // Istanza diretta del DAO; in un progetto più grande potresti utilizzare @Autowired
    private ToDoDAO toDoDAO = new ToDoDAOImpl();

    // Ottiene tutti i To Do
    @GetMapping
    public List<ToDoItem> getAllToDos() {
        return toDoDAO.getAllToDos();
    }

    // Crea un nuovo To Do
    @PostMapping
    public ToDoItem createToDo(@RequestBody ToDoItem todo) {
        boolean inserted = toDoDAO.insertToDo(todo);
        if (!inserted) {
            throw new RuntimeException("Errore nell'inserimento del To Do");
        }
        return todo;
    }

    // Aggiorna un To Do esistente (opzionale)
    @PutMapping("/{id}")
    public ToDoItem updateToDo(@PathVariable int id, @RequestBody ToDoItem todo) {
        todo.setId(id);
        boolean updated = toDoDAO.updateToDo(todo);
        if (!updated) {
            throw new RuntimeException("Errore nell'aggiornamento del To Do con ID " + id);
        }
        return todo;
    }

    // Elimina un To Do
    @DeleteMapping("/{id}")
    public String deleteToDo(@PathVariable int id) {
        boolean deleted = toDoDAO.deleteToDo(id);
        if (!deleted) {
            throw new RuntimeException("Errore nell'eliminazione del To Do con ID " + id);
        }
        return "To Do eliminato con successo";
    }
}
