package com.gaugetest.utils;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
    public static String readFieldFromExcel(int rowParam,int cellParam) {
        String readedValue = "";
        try {
            // Excel dosyasının yolunu belirtin
            String dosyaYolu = "testDataFiles/data.xlsx";

            // FileInputStream ile Excel dosyasını oku
            FileInputStream fileInputStream = new FileInputStream(dosyaYolu);

            // Workbook ve Sheet objelerini oluştur
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // İlk sayfayı al

            // İlk satırı ve ilk sütunu seçerek hücreyi al
            Row row = sheet.getRow(rowParam); // 1. satır
            Cell cell = row.getCell(cellParam); // 1. sütun

            // Hücredeki değeri al
            readedValue = cell.getStringCellValue();

            // Okunan değeri kullan
            System.out.println("Readed value " + readedValue);

            // FileInputStream ve Workbook'ı kapat
            fileInputStream.close();
            workbook.close();

        } catch (IOException | EncryptedDocumentException e) {
            e.printStackTrace();
        }
        return readedValue;
    }
}