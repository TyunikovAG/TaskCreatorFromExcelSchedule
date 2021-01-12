package excel;

import new_schedule.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestExelCreator {

    static TaskBlank testerForTaskBlank;

    public static void main(String[] args) {

        initTesterBlank();
        new ExelCreator().createTaskBlank(testerForTaskBlank, "бланк.xlsx");
    }

    private static void initTesterBlank() {
        Team team = new Team("Звено №1", "Задача для команды");
        ArrayList<Worker> workers = new ArrayList<>(2);
        workers.add(new Worker("Иванов А.И.", "сантехник"));
        workers.add(new Worker("Петров Г.В.", "бутыльник"));
        team.setMembers(workers);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team);
        testerForTaskBlank = new TaskBlank(LocalDate.now(), Shift.EVENING);
        testerForTaskBlank.setTeams(teams);
    }
}
