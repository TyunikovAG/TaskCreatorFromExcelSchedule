package new_schedule;

import java.util.ArrayList;

public class Team {

    public Team(String teamNumber, String task) {
        this.teamNumber = teamNumber;
        this.task = task;
    }

    private String teamNumber;
    private ArrayList<Worker> members;
    private String task;

    public void setMembers(ArrayList<Worker> members) {
        this.members = members;
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public ArrayList<Worker> getMembers() {
        return members;
    }

    public String getTask() {
        return task;
    }
}
