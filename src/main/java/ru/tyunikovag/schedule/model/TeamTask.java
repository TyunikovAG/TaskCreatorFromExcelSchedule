package ru.tyunikovag.schedule.model;

import java.time.LocalDate;
import java.util.List;

public class TeamTask {

    private final LocalDate data;
    private final Shift shift;
    private List<Team> teams;
    private String author;

    public TeamTask(LocalDate data, Shift shift) {
        this.data = data;
        this.shift = shift;
        author = "Семёнов И.П.";
    }

    public TeamTask(LocalDate data, Shift shift, String author) {
        this(data, shift);
        this.author = author;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
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

    public List<Team> getTeams() {
        return teams;
    }
}
