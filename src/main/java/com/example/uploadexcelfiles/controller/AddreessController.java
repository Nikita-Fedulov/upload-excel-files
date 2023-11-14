package com.example.uploadexcelfiles.controller;

import com.example.uploadexcelfiles.DTO.AddressDTO;
import com.example.uploadexcelfiles.DTO.AddressDetailsDTO;
import com.example.uploadexcelfiles.DTO.AddressResponseDTO;
import com.example.uploadexcelfiles.exception.RateLimitExceededException;
import com.example.uploadexcelfiles.model.AddressDetail;
import com.example.uploadexcelfiles.model.AddressValue;
import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.model.RequestCount;
import com.example.uploadexcelfiles.repository.AddressDetailRepository;
import com.example.uploadexcelfiles.repository.AddressValueRepository;
import com.example.uploadexcelfiles.repository.RequestCountRepository;
import com.example.uploadexcelfiles.service.ExcelFileService;
import com.example.uploadexcelfiles.service.RequestCountService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
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
        headerRow.createCell(1).setCellValue("OBJECT GUID");
        headerRow.createCell(2).setCellValue("OPERATION ID");
        headerRow.createCell(3).setCellValue("REGION");
        headerRow.createCell(4).setCellValue("FULL NAME");
        headerRow.createCell(5).setCellValue("OBJECT ID");


        int rowNum = 1;
        for (AddressValue addressValue : addressValues) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(addressValue.getId());
            row.createCell(1).setCellValue(addressValue.getObjectGuid());
            row.createCell(2).setCellValue(addressValue.getOperationTypeId());
            row.createCell(3).setCellValue(addressValue.getRegionCode());
            row.createCell(4).setCellValue(addressValue.getFullName());
            row.createCell(5).setCellValue(addressValue.getObjectId());
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




