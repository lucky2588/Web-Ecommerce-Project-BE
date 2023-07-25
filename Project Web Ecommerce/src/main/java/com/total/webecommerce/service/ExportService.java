package com.total.webecommerce.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.total.webecommerce.entity.Payment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportService {


    public byte[] exportFile() throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        Font titleFont = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, 20, Font.BOLD, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont("Arial Unicode MS", BaseFont.IDENTITY_H, 12, Font.NORMAL, BaseColor.BLACK);
        PdfWriter prdWriter = PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph title = new Paragraph();
        title.setAlignment(Element.ALIGN_CENTER);
        Chunk reduve = new Chunk("Bill", titleFont);
        Chunk ID = new Chunk("ID", titleFont);
        title.add(reduve);
        title.add(Chunk.NEWLINE);
        title.add(ID);
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.close();
        return outputStream.toByteArray();
    }
    public File exportExcal() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        String title = "Files.xlsx";
        File file = new File(title);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet nameFile = workbook.createSheet("Clone Data");
            Row row = nameFile.createRow(0);
            row.createCell(0).setCellValue("ID");      // đặt tên cột O là cột ầu ...
            row.createCell(1).setCellValue("ID");      // đặt tên cột
            Integer rowNum = 1;           //
            for (Integer id : ids) {
                Row rowX = nameFile.createRow(rowNum++); // gắn tưởng ướng với giá trị cho các cột
                rowX.createCell(0).setCellValue(id);
                rowX.createCell(1).setCellValue(id);
            }
            for (int i = 0; i < 2; i++) {           //
                nameFile.autoSizeColumn(i);
            }
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return file;
    }
}
