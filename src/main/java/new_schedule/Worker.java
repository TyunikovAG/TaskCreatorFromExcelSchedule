package new_schedule;

public class Worker {

    String fio;
    String proffesion;

    public Worker(String fio, String proffesion) {
        this.fio = fio;
        this.proffesion = proffesion;
    }

    public String getFio() {
        return fio;
    }

    public String getProffesion() {
        return proffesion;
    }

    @Override
    public String toString() {
        return fio;
    }
}
