package com.taskhub.dao;

import com.taskhub.model.Task;
import java.util.List;

public interface TaskDAO {
    List<Task> getAllTasks();
    Task getTaskById(int id);
    boolean insertTask(Task task);
    boolean updateTask(Task task);
    boolean deleteTask(int id);
}
