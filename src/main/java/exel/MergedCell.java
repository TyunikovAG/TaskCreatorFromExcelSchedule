package exel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class MergedCell {

    public static void main(String[] args) throws IOException {
        String filename = "бланк.xlsx";
        FileInputStream fis = new FileInputStream(filename);
        Workbook workbook = new XSSFWorkbook(fis);
        fis.close();

        Sheet sheet = workbook.getSheetAt(0);

        Row row = sheet.getRow(6);
        Cell cellMerg = row.getCell(0);
        System.out.println(cellMerg.getStringCellValue());
        cellMerg.setCellValue("Hello my friend");
//
//        cell1.setCellValue(s1);
//        cell2.setCellValue(s2);
//
//        CellStyle cs = workbook.createCellStyle();
//        cs.setWrapText(true);
//        cs.setAlignment(HorizontalAlignment.LEFT);
//        cs.setVerticalAlignment(VerticalAlignment.TOP);
//        cell1.setCellStyle(cs);
//        cell2.setCellStyle(cs);
//
//        sheet.setActiveCell(new CellAddress(1, 1));
//
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.close();

        Desktop desktop = Desktop.getDesktop();
        desktop.edit(new File(filename));
    }
}
