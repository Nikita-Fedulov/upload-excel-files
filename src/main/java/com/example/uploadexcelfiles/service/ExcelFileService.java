package com.example.uploadexcelfiles.service;

import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.repository.ExcelFileRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelFileService {
    @Autowired
    ExcelFileRepository repository;

    public void uploadAndParseExcelFile(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<String> addresses = new ArrayList<>();

        for (Row row : sheet) {
            String address = row.getCell(1).getStringCellValue();
            addresses.add(address);
        }

        List<ExcelAddress> existingAddresses = repository.findByAddressIn(addresses);

        for (String address : addresses) {
            if (existingAddresses.stream().noneMatch(a -> a.getAddress().equals(address))) {
                ExcelAddress newAddress = new ExcelAddress();
                newAddress.setAddress(address);
                repository.save(newAddress);
            }
        }

        workbook.close();
    }

    public List<ExcelAddress> getDataFromExcel(MultipartFile file) throws IOException {
        List<ExcelAddress> dataFromExcel = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(1);

            if (cell != null) {
                String address = cell.getStringCellValue();
                ExcelAddress excelAddress = new ExcelAddress();
                excelAddress.setAddress(address);
                dataFromExcel.add(excelAddress);
            }
        }

        workbook.close();

        return dataFromExcel;
    }
}
