package ru.tyunikovag.schedule.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

    private Map<Integer, Team> teams = new HashMap<>();
    private LocalDate data;
    private String author;
    private Shift shift;

    public Task() {
        author = "Семёнов И.П.";
    }

    public Task(LocalDate data, Shift shift) {
        this.data = data;
        this.shift = shift;
    }

    public Task(LocalDate data, Shift shift, String author) {
        this(data, shift);
        this.author = author;
    }

    public LocalDate getData() {
        return data;
    }

    public Shift getShift() {
        return shift;
    }

    public String getAuthor() {
        return author;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
