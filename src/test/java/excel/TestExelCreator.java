package excel;

import ru.tyunikovag.schedule.*;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Task;
import ru.tyunikovag.schedule.model.Team;
import ru.tyunikovag.schedule.model.Worker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestExelCreator {

    static Task testerForTask;

    public static void main(String[] args) {

        initTesterBlank();
        new ExelCreator().createTaskBlanks(testerForTask, "бланк.xlsx");
    }

    private static void initTesterBlank() {
        Team team = new Team("Звено №1", "Задача для команды");
        ArrayList<Worker> workers = new ArrayList<>(2);
        workers.add(new Worker("Иванов А.И.", "сантехник"));
        workers.add(new Worker("Петров Г.В.", "бутыльник"));
        team.setMembers(workers);
        Map<Integer, Team> teams = new HashMap<>();
        teams.put(1, team);
        testerForTask = new Task(LocalDate.now(), Shift.EVENING);
        testerForTask.setTeams(teams);
    }
}
