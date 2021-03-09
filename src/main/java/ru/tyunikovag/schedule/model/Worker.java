package ru.tyunikovag.schedule.model;

public class Worker {

    private final String fio;
    private final String profession;

    public Worker(String fio, String profession) {
        this.fio = fio;
        this.profession = profession;
    }

    public String getFio() {
        return fio;
    }

    public String getProfession() {
        return profession;
    }

    @Override
    public String toString() {
        return fio;
    }
}
