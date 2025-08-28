package com.example.todo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private boolean done = false;
    private LocalDate dueDate;

    // Trello-koppling
    private String trelloCardId;
    private String trelloShortUrl;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getTrelloCardId() { return trelloCardId; }
    public void setTrelloCardId(String trelloCardId) { this.trelloCardId = trelloCardId; }
    public String getTrelloShortUrl() { return trelloShortUrl; }
    public void setTrelloShortUrl(String trelloShortUrl) { this.trelloShortUrl = trelloShortUrl; }
}
