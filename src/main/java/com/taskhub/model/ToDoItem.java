package com.taskhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ToDoItem {
    private int id;
    private int utenteId;
    private String testoTodolist;
    private boolean completato;
    
    // Utilizza JsonFormat se desideri formattare la data in JSON
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCreazione;

    public ToDoItem() {}

    public ToDoItem(int id, int utenteId, String testoTodolist, boolean completato, LocalDateTime dataCreazione) {
        this.id = id;
        this.utenteId = utenteId;
        this.testoTodolist = testoTodolist;
        this.completato = completato;
        this.dataCreazione = dataCreazione;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUtenteId() {
        return utenteId;
    }
    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }
    public String getTestoTodolist() {
        return testoTodolist;
    }
    public void setTestoTodolist(String testoTodolist) {
        this.testoTodolist = testoTodolist;
    }
    public boolean isCompletato() {
        return completato;
    }
    public void setCompletato(boolean completato) {
        this.completato = completato;
    }
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "id=" + id +
                ", utenteId=" + utenteId +
                ", testoTodolist='" + testoTodolist + '\'' +
                ", completato=" + completato +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}
