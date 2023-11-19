package com.example.uploadexcelfiles.controller;

import com.example.uploadexcelfiles.model.AddressDetail;
import com.example.uploadexcelfiles.model.AddressValue;
import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.repository.AddressValueRepository;
import com.example.uploadexcelfiles.service.ExcelFileService;
import com.example.uploadexcelfiles.service.RequestCountService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Controller
public class AddreessController {
    private final AddressValueRepository addressValueRepository;
    private final ExcelFileService excelFileService;
    private final RequestCountService requestCountService;

    public AddreessController(AddressValueRepository addressValueRepository, ExcelFileService excelFileService, RequestCountService requestCountService) {
        this.addressValueRepository = addressValueRepository;
        this.excelFileService = excelFileService;
        this.requestCountService = requestCountService;
    }

    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            excelFileService.uploadAndParseExcelFile(file);
            List<ExcelAddress> dataFromExcel = excelFileService.getDataFromExcel(file);
            requestCountService.processAddressesAndSaveCount(dataFromExcel);
            return "redirect:/success";
        } catch (Exception e) {
            return "Произошла ошибка";
        }
    }


    @GetMapping("/success")
    public String showSuccessPage() {
        return "result";
    }

    @GetMapping("/downloadExcel")
    public ResponseEntity<InputStreamResource> generateExcelFile() throws FileNotFoundException {
        List<AddressValue> addressValues = addressValueRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Адреса");


        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("You address");
        headerRow.createCell(2).setCellValue("OBJECT GUID");
        headerRow.createCell(3).setCellValue("OPERATION ID");
        headerRow.createCell(4).setCellValue("REGION");
        headerRow.createCell(5).setCellValue("FULL NAME");
        headerRow.createCell(6).setCellValue("OBJECT ID");
        headerRow.createCell(7).setCellValue("Kladr code");


        int rowNum = 1;
        for (AddressValue addressValue : addressValues) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(addressValue.getId());
            row.createCell(1).setCellValue(addressValue.getExcelAddressValue());
            row.createCell(2).setCellValue(addressValue.getObjectGuid());
            row.createCell(3).setCellValue(addressValue.getOperationTypeId());
            row.createCell(4).setCellValue(addressValue.getRegionCode());
            row.createCell(5).setCellValue(addressValue.getFullName());
            row.createCell(6).setCellValue(addressValue.getObjectId());
            AddressDetail addressDetail = addressValue.getAddressDetail();
            if (addressDetail != null) {
                row.createCell(7).setCellValue(addressDetail.getKladr_code());
            } else {
                row.createCell(7).setCellValue("Null"); // Или другое значение по умолчанию, когда addressDetail равно null
            }
        }

        // Сохранить документ Excel
        String fileName = "address_values.xlsx";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Отправить файл пользователю в качестве вложенного файла
        File file = new File(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}




