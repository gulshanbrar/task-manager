package com.taskhub.controller;

import com.taskhub.dao.TaskDAO;
import com.taskhub.dao.TaskDAOImpl;
import com.taskhub.model.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // Per semplicità istanziamo direttamente l'implementazione DAO
    private TaskDAO taskDAO = new TaskDAOImpl();

    // Ottiene tutte le task
    @GetMapping
    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }

    // Crea una nuova task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        boolean inserted = taskDAO.insertTask(task);
        if (!inserted) {
            throw new RuntimeException("Errore nell'inserimento della task");
        }
        return task;
    }
    
    // Aggiorna una task (opzionale)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task task) {
        task.setId(id);
        boolean updated = taskDAO.updateTask(task);
        if (!updated) {
            throw new RuntimeException("Errore nell'aggiornamento della task con ID " + id);
        }
        return task;
    }
    
    // Elimina una task (opzionale)
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {
        boolean deleted = taskDAO.deleteTask(id);
        if (!deleted) {
            throw new RuntimeException("Errore nell'eliminazione della task con ID " + id);
        }
        return "Task eliminata con successo";
    }
}
