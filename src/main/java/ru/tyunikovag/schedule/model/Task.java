package ru.tyunikovag.schedule.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Task {

    private Map<Integer, Team> teams = new HashMap<>();
    private LocalDate date;
    private String author;
    private Shift shift;

    public Task() {
        author = "Семёнов И.П.";
    }

    public Task(LocalDate date, Shift shift) {
        this.date = date;
        this.shift = shift;
    }

    public Task(LocalDate date, Shift shift, String author) {
        this(date, shift);
        this.author = author;
    }

    public LocalDate getDate() {
        return date;
    }

    public Shift getShift() {
        return shift;
    }

    public String getAuthor() {
        return author;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Map<Integer, Team> getTeams() {
        return teams;
    }

    public void setTeams(Map<Integer, Team> teams) {
        this.teams = teams;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
