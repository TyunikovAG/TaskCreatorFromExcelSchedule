package ru.tyunikovag.schedule.model;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String teamNumber;
    private List<Worker> members = new ArrayList<>();
    private String teamTask;


    public Team(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Team(String teamNumber, String task) {
        this(teamNumber);
        this.teamTask = task;
    }


    public void setMembers(ArrayList<Worker> members) {
        this.members = members;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public List<Worker> getMembers() {
        return members;
    }

    public String getTeamTask() {
        return teamTask;
    }

    public void setTeamTask(String teamTask) {
        this.teamTask = teamTask;
    }
}
