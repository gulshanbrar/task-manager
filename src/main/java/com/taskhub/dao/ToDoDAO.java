package com.taskhub.dao;

import com.taskhub.model.ToDoItem;
import java.util.List;

public interface ToDoDAO {
    List<ToDoItem> getAllToDos();
    ToDoItem getToDoById(int id);
    boolean insertToDo(ToDoItem todo);
    boolean updateToDo(ToDoItem todo);
    boolean deleteToDo(int id);
}
