package ru.tyunikovag.schedule.controller;

import javafx.application.Platform;
import ru.tyunikovag.schedule.ExelCreator;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Task;
import ru.tyunikovag.schedule.model.Team;
import ru.tyunikovag.schedule.model.Worker;
import ru.tyunikovag.schedule.providers.ExelProvider;
import ru.tyunikovag.schedule.providers.PropertyProvider;
import ru.tyunikovag.schedule.view.TaskView;

import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskController {

    private TaskView view;
    private Map<Worker, Map<Integer, Shift>> scheduleOfAllWorkers;
    private Task task = new Task();
    private Set<Worker> workersOnTask = new HashSet<>();

    private PropertyProvider propertyProvider;
    private ExelProvider exelProvider;
    private File scheduleFile;
    private static String taskBlankFileName;
    private static String scheduleFileName;
    private static int teamCount = 0;


    public TaskController(TaskView view) {
        this.view = view;
        initProperty();
        exelProvider = new ExelProvider();
        scheduleFile = new File(scheduleFileName);
        readScheduleAndWorkers(LocalDate.now());
        view.setTaskAuthors(getAuthors());
        view.setLblFileName(scheduleFileName);
    }

    private void initProperty() {

        propertyProvider = PropertyProvider.getInstance();
        scheduleFileName = propertyProvider.get(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
        taskBlankFileName = propertyProvider.get(PropertyProvider.PropertyName.TASK_BLANK_FILE_NAME);
    }

    public String getScheduleFileName() {
        return scheduleFileName;
    }

    public List<String> getAuthors() {
        return propertyProvider.getAuthors();
    }

    private void readScheduleAndWorkers(LocalDate localDate) {
        scheduleOfAllWorkers = exelProvider.getScheduleOfAllWorkers(scheduleFile, localDate);
    }

    public List<Worker> getWorkersOnShift(int dayOfMonth, Shift shift) {
//        ArrayList<Worker> workersOnShift = new ArrayList<>(10);
//        for (Map.Entry<Worker, Map<Integer, Shift>> personMap : scheduleOfAllWorkers.entrySet()) {
//            Map<Integer, Shift> schedule = personMap.getValue();
//            Shift personShift = schedule.get(dayOfMonth);
//            if (personShift != null) {
//                if (personShift.equals(shift)) {
//                    workersOnShift.add(personMap.getKey());
//                }
//            }
//        }
//        return workersOnShift;
        List<Worker> workersOnShiftList = scheduleOfAllWorkers.entrySet().stream()
                .filter(entry -> entry.getValue().get(dayOfMonth) != null)
                .filter(entry -> entry.getValue().get(dayOfMonth).equals(shift))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        workersOnTask.addAll(workersOnShiftList);
        return workersOnShiftList;
    }

    public int addTeam() {
        int teamNumber = task.getTeams().size() + 1;
        task.getTeams().put(teamNumber, new Team(String.valueOf(teamNumber)));
        // TODO: 14.03.2021 complete this
        return teamNumber;
    }

    public void deleteLastTeam() {
        int lastTeam = task.getTeams().size();
        task.getTeams().remove(lastTeam);
    }

    public void sendTaskToBlank() {
        // TODO: 14.03.2021 may I create NEW ExelCreator?
        new ExelCreator().createTaskBlank(task, taskBlankFileName);
    }

    public void scheduleFileChanged() {
        scheduleFileName = propertyProvider.get(PropertyProvider.PropertyName.SCHEDULE_FILE_NAME);
        view.setLblFileName(scheduleFileName);
        // TODO: 14.03.2021 it must reread changed schedule
    }

    public List<String> getAllWorkersNames() {
        if (scheduleOfAllWorkers != null) {
            return scheduleOfAllWorkers.keySet().stream()
                    .map(Worker::getFio)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    public void addAnotherWorker(String fio, String profession) {
        Worker worker = new Worker(fio, profession);
        if (isWorkersIsInTask(worker)) {
            JOptionPane.showMessageDialog(null,
                    "Сотрудник " + worker.getFio() + " уже есть в списке", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        view.addWorkerLabelToLeftBox(worker.getFio());
    }

    private boolean isWorkersIsInTask(Worker worker) {
//        return getWorkerFromTaskWorkersByFio(worker.getFio()) != null;
        return workersOnTask.stream().anyMatch(w -> w.getFio().equals(worker.getFio()));
    }

    private void checkForMe(Map<Worker, Map<Integer, Shift>> scheduleOfAllWorkers) {
        boolean toExit = true;
        for (Worker worker : scheduleOfAllWorkers.keySet()) {
            if (worker.getFio().contains("Тюников")) {
                toExit = false;
                break;
            }
        }
        if (toExit) {
            JOptionPane.showMessageDialog(null,
                    "Неверное структурное подразделение", "Error", JOptionPane.ERROR_MESSAGE);
            Platform.exit();

        }
    }

    public void addMemberToTeam(int teamNumber, String fio) {
        Team team = task.getTeams().get(teamNumber);
        Worker worker = getWorkerByFio(fio);
        team.getMembers().add(worker);

    }

    public void removeMemberFromTeam(int teamNumber, String fio) {
        Team team = task.getTeams().get(teamNumber);
        Worker worker = getWorkerByFio(fio);
        team.getMembers().remove(worker);
    }

    private Worker getWorkerByFio(String fio) {
        return scheduleOfAllWorkers.keySet().stream().filter(w -> w.getFio().equals(fio)).findFirst().orElseGet(null);
    }

    public void resetTask() {
        workersOnTask.clear();
        task.getTeams().clear();
    }

    public void setShift(Shift shift) {
        task.setShift(shift);
    }

    public void setTaskDate(LocalDate date) {
        task.setData(date);
    }

    public void setTeamTask(int teamNumber, String teamTask) {
        task.getTeams().get(teamNumber).setTask(teamTask);
    }

    public void changeAuthor(String author) {
        task.setAuthor(author);
    }
}
