package com.moika22.canteen;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ReportBuilder {

    private final Logger logger;

    public ReportBuilder() {
        logger = LoggerFactory.getLogger(ReportBuilder.class);
    }

    public void createReport(Map<String, Integer> persons, String filePath) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Employees");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Имя");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Количество посещений за сегодня");
        headerCell.setCellStyle(headerStyle);

        int rowNumber = 2;

        for (String employee : persons.keySet()) {
            Row row = sheet.createRow(rowNumber);
            Cell cell = row.createCell(0);
            cell.setCellValue(employee);

            cell = row.createCell(1);
            cell.setCellValue(persons.get(employee));
            rowNumber++;
        }

        try {
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            logger.error("Не удалось создать файл");
        } catch (IOException e) {
            logger.error("Не удалось записать в файл");
        }
    }
}
