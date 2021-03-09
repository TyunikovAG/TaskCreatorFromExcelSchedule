package excel;

import ru.tyunikovag.schedule.*;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.TeamTask;
import ru.tyunikovag.schedule.model.Team;
import ru.tyunikovag.schedule.model.Worker;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestExelCreator {

    static TeamTask testerForTeamTask;

    public static void main(String[] args) {

        initTesterBlank();
        new ExelCreator().createTaskBlank(testerForTeamTask, "бланк.xlsx");
    }

    private static void initTesterBlank() {
        Team team = new Team("Звено №1", "Задача для команды");
        ArrayList<Worker> workers = new ArrayList<>(2);
        workers.add(new Worker("Иванов А.И.", "сантехник"));
        workers.add(new Worker("Петров Г.В.", "бутыльник"));
        team.setMembers(workers);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team);
        testerForTeamTask = new TeamTask(LocalDate.now(), Shift.EVENING);
        testerForTeamTask.setTeams(teams);
    }
}
