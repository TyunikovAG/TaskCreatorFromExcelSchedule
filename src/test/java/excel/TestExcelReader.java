package excel;

import ru.tyunikovag.schedule.providers.ExelProvider;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Worker;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TestExcelReader {
    
    public static void main(String[] args) {

        ExelProvider exelProvider = new ExelProvider();
        File scheduleFile = new File("C:\\JAVA\\workspace\\FXtesting\\График 2020.xlsx");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse("01.01.2021", formatter);
        Map<Worker, Map<Integer, Shift>> shedule = exelProvider.getScheduleOfAllWorkers(scheduleFile, date);
        System.out.println(shedule);
    }
}
