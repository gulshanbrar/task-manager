package com.taskhub.dao;

import com.taskhub.model.Task;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Task task = mapRowToTask(rs);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Task getTaskById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTask(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertTask(Task task) {
        String sql = "INSERT INTO tasks (utente_id, titolo, descrizione, data_task, priorita, stato) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, task.getUtenteId());
            stmt.setString(2, task.getTitolo());
            stmt.setString(3, task.getDescrizione());
            stmt.setDate(4, Date.valueOf(task.getDataTask()));
            stmt.setString(5, task.getPriorita());
            stmt.setString(6, task.getStato());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        task.setId(generatedKeys.getInt(1));
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
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET utente_id = ?, titolo = ?, descrizione = ?, data_task = ?, priorita = ?, stato = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, task.getUtenteId());
            stmt.setString(2, task.getTitolo());
            stmt.setString(3, task.getDescrizione());
            stmt.setDate(4, Date.valueOf(task.getDataTask()));
            stmt.setString(5, task.getPriorita());
            stmt.setString(6, task.getStato());
            stmt.setInt(7, task.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
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

    // Metodo di utilità per mappare una riga del ResultSet a un oggetto Task
    private Task mapRowToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setUtenteId(rs.getInt("utente_id"));
        task.setTitolo(rs.getString("titolo"));
        task.setDescrizione(rs.getString("descrizione"));
        task.setDataTask(rs.getDate("data_task").toLocalDate());
        task.setPriorita(rs.getString("priorita"));
        task.setStato(rs.getString("stato"));
        Timestamp timestamp = rs.getTimestamp("data_creazione");
        if (timestamp != null) {
            task.setDataCreazione(timestamp.toLocalDateTime());
        }
        return task;
    }
}
