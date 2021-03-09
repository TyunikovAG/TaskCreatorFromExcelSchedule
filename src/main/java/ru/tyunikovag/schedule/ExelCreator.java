package ru.tyunikovag.schedule;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.tyunikovag.schedule.model.Shift;
import ru.tyunikovag.schedule.model.TeamTask;
import ru.tyunikovag.schedule.model.Team;
import ru.tyunikovag.schedule.model.Worker;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class ExelCreator {

    int rowForTeam = 17;
    int colLeftTeam = 0;
    int colRightTeam = 5;
    int rowOfDate = 6;
    int rowOfShift = 7;
    File blankFile;
    File parentDir;
    String createdFileNname;

    public void createTaskBlank(TeamTask teamTask, String taskBlankFileName) {

        blankFile = new File(taskBlankFileName);
        if (blankFile.exists()){
            try {
                Workbook workbook = new XSSFWorkbook(blankFile);
                fillBlank(workbook, teamTask);
                parentDir = blankFile.getAbsoluteFile().getParentFile();
                createdFileNname = String.format("%s/Наряд на %s - %s.xlsx",
                        parentDir, teamTask.getData().toString(), rusShift(teamTask.getShift()));

                FileOutputStream fos = new FileOutputStream(createdFileNname);
                workbook.write(fos);
                fos.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.edit(new File(createdFileNname));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private String rusShift(Shift shift){
        switch (shift){
            case NIGHT:{
                return "ночь";
            } case MORNING: {
                return "день";
            } case EVENING: {
                return "вечер";
            } default: {
                return "Error";
            }
        }
    }

    private void fillBlank(Workbook workbook, TeamTask teamTask) {
        Sheet sheet = workbook.getSheetAt(0);

        fillHeaderDate(sheet, teamTask.getData());
        fillHeaderShift(sheet, teamTask.getShift());
        for (Team team : teamTask.getTeams()){
            fillTeamLine(sheet, team);
            rowForTeam++;
        }
    }

    private void fillHeaderDate(Sheet sheet, LocalDate date) {
        Row row = sheet.getRow(rowOfDate);
        Cell leftCell = row.getCell(0);
        Cell rightCell = row.getCell(5);
        String dateString = "НА    \"  %s   \"        %s             %s г.";

        dateString = String.format(dateString, date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        leftCell.setCellValue(dateString);
        rightCell.setCellValue(dateString);
    }

    private void fillHeaderShift(Sheet sheet, Shift shift){
        String shiftBlank = "Fatal error";

        switch (shift){
            case NIGHT: {
                shiftBlank = "СМЕНА   с      01.00 ч.    до      09.00  ч.";
                break;
            } case MORNING: {
                shiftBlank = "СМЕНА   с      09.00 ч.    до      17.00  ч.";
                break;
            } case EVENING: {
                shiftBlank = "СМЕНА   с      17.00 ч.    до      01.00  ч.";
            }
        }

        Row row = sheet.getRow(rowOfShift);
        row.getCell(0).setCellValue(shiftBlank);
        row.getCell(5).setCellValue(shiftBlank);
    }

    private void fillTeamLine(Sheet sheet, Team team){

        CellStyle lbMulti = getLeftTopMultilineStyle(sheet.getWorkbook());
        XSSFFont fontBold= (XSSFFont) sheet.getWorkbook().createFont();
        fontBold.setBold(true); //set bold
        fontBold.setFontHeight(11); //add font size

        Row row = sheet.getRow(rowForTeam);
        Cell leftCell = row.createCell(colLeftTeam);
        Cell rightCell = row.createCell(colRightTeam);
        XSSFRichTextString members = new XSSFRichTextString();
        if (team.getMembers().size() > 1){
            members.append("старший\n");
        }
        for (Worker worker : team.getMembers()){
            members.append(worker.getFio() + "\n", fontBold);
            members.append(worker.getProfession() + "\n");
        }
        fillTwoCell(leftCell, row, members, team.getTask(), lbMulti);
        fillTwoCell(rightCell, row, members, team.getTask(), lbMulti);
    }

    private void fillTwoCell(Cell cell, Row row, XSSFRichTextString members, String task, CellStyle style){
        cell.setCellValue(members);
        cell.setCellStyle(style);
        cell = row.createCell(cell.getColumnIndex() + 1);
        cell.setCellValue(task);
        cell.setCellStyle(style);
    }

    private CellStyle getLeftTopMultilineStyle(Workbook workbook){
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        cs.setAlignment(HorizontalAlignment.LEFT);
        cs.setVerticalAlignment(VerticalAlignment.TOP);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        return cs;
    }
}
