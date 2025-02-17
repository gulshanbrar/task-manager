package com.taskhub.dao;

import com.taskhub.model.ToDoItem;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ToDoDAOImpl implements ToDoDAO {

    @Override
    public List<ToDoItem> getAllToDos() {
        List<ToDoItem> todos = new ArrayList<>();
        String sql = "SELECT * FROM todolist";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ToDoItem todo = mapRowToToDo(rs);
                todos.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    @Override
    public ToDoItem getToDoById(int id) {
        String sql = "SELECT * FROM todolist WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToToDo(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertToDo(ToDoItem todo) {
        String sql = "INSERT INTO todolist (utente_id, testo_todolist, completato) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, todo.getUtenteId());
            stmt.setString(2, todo.getTestoTodolist());
            stmt.setBoolean(3, todo.isCompletato());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        todo.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateToDo(ToDoItem todo) {
        String sql = "UPDATE todolist SET utente_id = ?, testo_todolist = ?, completato = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, todo.getUtenteId());
            stmt.setString(2, todo.getTestoTodolist());
            stmt.setBoolean(3, todo.isCompletato());
            stmt.setInt(4, todo.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteToDo(int id) {
        String sql = "DELETE FROM todolist WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mappa una riga del ResultSet a un oggetto ToDoItem
    private ToDoItem mapRowToToDo(ResultSet rs) throws SQLException {
        ToDoItem todo = new ToDoItem();
        todo.setId(rs.getInt("id"));
        todo.setUtenteId(rs.getInt("utente_id"));
        todo.setTestoTodolist(rs.getString("testo_todolist"));
        todo.setCompletato(rs.getBoolean("completato"));
        Timestamp ts = rs.getTimestamp("data_creazione");
        if (ts != null) {
            todo.setDataCreazione(ts.toLocalDateTime());
        }
        return todo;
    }
}
