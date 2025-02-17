package com.taskhub;

import com.taskhub.dao.TaskDAO;
import com.taskhub.dao.TaskDAOImpl;
import com.taskhub.model.Task;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskDAO taskDAO = new TaskDAOImpl();

        // Inserimento di una nuova task
        Task newTask = new Task();
        newTask.setUtenteId(1); // Assicurati che l'utente con ID 1 esista nel database
        newTask.setTitolo("Batteria di Giovanni");
        newTask.setDescrizione("So cazzi, bisogna aggiustare tutto");
        newTask.setDataTask(LocalDate.of(2026, 2, 21));
        newTask.setPriorita("Alta");
        newTask.setStato("In corso");

        boolean inserted = taskDAO.insertTask(newTask);
        if (inserted) {
            System.out.println("Task inserita con ID: " + newTask.getId());
        } else {
            System.out.println("Errore nell'inserimento della task.");
        }

        // Recupera e visualizza tutte le task
        List<Task> tasks = taskDAO.getAllTasks();
        System.out.println("Lista delle task:");
        for (Task task : tasks) {
            System.out.println(task);
        }

        // Esempio di aggiornamento: impostiamo la prima task come "Completata"
        if (!tasks.isEmpty()) {
            Task taskToUpdate = tasks.get(0);
            taskToUpdate.setStato("Completata");
            boolean updated = taskDAO.updateTask(taskToUpdate);
            System.out.println("Task aggiornata: " + updated);
        }

        // Esempio di eliminazione (scommenta se desideri cancellare una task)
        // boolean deleted = taskDAO.deleteTask(newTask.getId());
        // System.out.println("Task eliminata: " + deleted);
    }
}
