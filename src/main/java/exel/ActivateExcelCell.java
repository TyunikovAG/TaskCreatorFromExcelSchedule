package exel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivateExcelCell {

    public static void main(String[] args) throws IOException {
        String filename = "book.xlsx";
        FileInputStream fis = new FileInputStream(filename);
        Workbook workbook = new XSSFWorkbook(fis);
        fis.close();

        Sheet sheet = workbook.getSheetAt(0);

        Cell cell1 = sheet.createRow(5).createCell(4);
        Cell cell2 = sheet.getRow(5).createCell(5);
        String s1 = "line1\nline2\nline3";
        String s2 = "привет как дела вот такой вот большшой текст задания\n" +
                "сначала проверить как там дела\n" +
                "потом выпить пива\n";

        cell1.setCellValue(s1);
        cell2.setCellValue(s2);

        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);
        cs.setAlignment(HorizontalAlignment.LEFT);
        cs.setVerticalAlignment(VerticalAlignment.TOP);
        cell1.setCellStyle(cs);
        cell2.setCellStyle(cs);

        sheet.setActiveCell(new CellAddress(1, 1));

        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.close();

        Desktop desktop = Desktop.getDesktop();
        desktop.edit(new File(filename));
    }
}
