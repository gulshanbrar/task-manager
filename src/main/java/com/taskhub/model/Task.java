package com.taskhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private int utenteId;
    private String titolo;
    private String descrizione;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataTask;
    private String priorita;  // Esempi: "Bassa", "Media", "Alta"
    private String stato;     // Esempi: "Da iniziare", "In corso", "Completata"
    private LocalDateTime dataCreazione;

    public Task() {}

    public Task(int id, int utenteId, String titolo, String descrizione, LocalDate dataTask, String priorita, String stato, LocalDateTime dataCreazione) {
        this.id = id;
        this.utenteId = utenteId;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataTask = dataTask;
        this.priorita = priorita;
        this.stato = stato;
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
    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public LocalDate getDataTask() {
        return dataTask;
    }
    public void setDataTask(LocalDate dataTask) {
        this.dataTask = dataTask;
    }
    public String getPriorita() {
        return priorita;
    }
    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }
    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", utenteId=" + utenteId +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataTask=" + dataTask +
                ", priorita='" + priorita + '\'' +
                ", stato='" + stato + '\'' +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}
