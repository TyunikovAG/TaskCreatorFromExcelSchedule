package new_schedule;

import java.time.LocalDate;
import java.util.List;

public class TaskBlank {

    private final LocalDate data;
    private final Shift shift;
    private List<Team> teams;
    private String auhtor;

    public TaskBlank(LocalDate data, Shift shift) {
        this.data = data;
        this.shift = shift;
        auhtor = "Семёнов И.П.";
    }

    public TaskBlank(LocalDate data, Shift shift, String auhtor) {
        this(data, shift);
        this.auhtor = auhtor;
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

    public String getAuhtor() {
        return auhtor;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
