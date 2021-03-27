package ru.tyunikovag.schedule.providers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.Worker;
import ru.tyunikovag.schedule.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExelProvider {

    private static final String CORNER_TEXT = "ФИО, должность";
    private static final Map<Worker, Map<Integer, Shift>> scheduleOfAllWorkers = new HashMap<>(50);
    Workbook workbook;
    Sheet sheet;
    int cornerRow;
    int cornerColumn;
    Cell cornerCell;

    public Map<Worker, Map<Integer, Shift>> getScheduleOfAllWorkers(File scheduleFile, LocalDate date) {
        try {
            FileInputStream fis = new FileInputStream(scheduleFile);
            workbook = new XSSFWorkbook(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = getScheetByDate(date);

        scheduleOfAllWorkers.clear();
        findCornerCell(sheet);
        fillEmployeeWorkSchedule();
        return scheduleOfAllWorkers;
    }

    private void findCornerCell(Sheet sheet) {
        boolean isMatch = false;
        for (Row row : sheet) {
            if (isMatch) break;
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().equals(CORNER_TEXT)) {
                        cornerRow = row.getRowNum();
                        cornerColumn = cell.getColumnIndex();
                        cornerCell = cell;
                        isMatch = true;
                        break;
                    }
                }
            }
        }
    }

    private void fillEmployeeWorkSchedule() {
        int lastRow = sheet.getLastRowNum();
        for (int i = cornerRow + 7; i < lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = sheet.getRow(i).getCell(cornerColumn);
                if (cell != null) {
                    if (sheet.getRow(i).getCell(cornerColumn).getCellType() == CellType.STRING) {
                        String fioString = cell.getStringCellValue();
                        Worker worker = new Worker(Util.getFIO(fioString), getProfession(fioString));
                        scheduleOfAllWorkers.put(worker, fillOneLine(cell.getRowIndex(), worker.getFio()));
                    }
                }
            } else {
                break;
            }
        }
    }

    private Map<Integer, Shift> fillOneLine(int rowIndex, String fio) {
        final int FIRST_DAY_INDENT = 2;
        Map<Integer, Shift> schedule = new HashMap<>(31);
        int dayCol = cornerColumn + FIRST_DAY_INDENT;
        int lastDayCol = dayCol + 31;
        for (int i = dayCol; i < lastDayCol; i++) {
            Cell cell_Top = sheet.getRow(rowIndex).getCell(i);
            Cell cell_Bottom = sheet.getRow(rowIndex + 1).getCell(i);
            String topValue = readStringFromCell(cell_Top);
            String bottomValue = readStringFromCell(cell_Bottom);

            if (topValue.equals("8.0")) {
                if (bottomValue.equals("3.0")) {
                    schedule.put(i - 2, Shift.EVENING);
                } else if (bottomValue.equals("5.0")) {
                    schedule.put(i - 2, Shift.NIGHT);
                } else {
                    schedule.put(i - 2, Shift.MORNING);
                }
            } else {
                schedule.put(i - 2, Shift.HOLIDAY);
            }
        }
        return schedule;
    }

    private String readStringFromCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: {
                return cell.getStringCellValue();
            }
            case NUMERIC: {
                return String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN: {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case _NONE: {
                return "";
            }
        }
        return "invalid format data in cell";
    }

    private String getProfession(String line) {
        if (line.endsWith("АСУ и ТП")) {
            return "АСУ и ТП";
        } else if (line.endsWith("слесарь")) {
            return "Эл-слесарь";
        } else if (line.endsWith("сварщик")) {
            return "Эл-сварщик";
        } else {
            return "неверный формат профессии";
        }
    }

    private Sheet getScheetByDate(LocalDate localDate) {

        String month = localDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        for (Sheet sheet : workbook) {
            String sheetName = sheet.getSheetName().toLowerCase();
            if (sheetName.contains(month.toLowerCase())) {
                return sheet;
            }
        }
        return null;
    }
}
