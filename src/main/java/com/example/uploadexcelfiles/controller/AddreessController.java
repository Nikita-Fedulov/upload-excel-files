package com.example.uploadexcelfiles.controller;

import com.example.uploadexcelfiles.DTO.AddressDTO;
import com.example.uploadexcelfiles.DTO.AddressDetailsDTO;
import com.example.uploadexcelfiles.DTO.AddressResponseDTO;
import com.example.uploadexcelfiles.exception.RateLimitExceededException;
import com.example.uploadexcelfiles.model.AddressDetail;
import com.example.uploadexcelfiles.model.AddressValue;
import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.repository.AddressDetailRepository;
import com.example.uploadexcelfiles.repository.AddressValueRepository;
import com.example.uploadexcelfiles.service.ExcelFileService;
import com.example.uploadexcelfiles.service.RequestLimitService;
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
import java.util.List;

@Controller
public class AddreessController {
    private final AddressValueRepository addressValueRepository;
    private final AddressDetailRepository addressDetailRepository;
    private final ExcelFileService excelFileService;
    private final RequestLimitService requestLimitService;

    public AddreessController(AddressValueRepository addressValueRepository, AddressDetailRepository addressDetailRepository, ExcelFileService excelFileService, RequestLimitService requestLimitService) {
        this.addressValueRepository = addressValueRepository;
        this.addressDetailRepository = addressDetailRepository;
        this.excelFileService = excelFileService;
        this.requestLimitService = requestLimitService;
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


            String url = "https://fias-public-service.nalog.ru/api/spas/v2.0/SearchAddressItems?search_string={name}&address_type=1";
            String token = "c0e8e0dd-3330-4772-b69d-e454da4fc865";
            HttpHeaders headers = new HttpHeaders();
            headers.add("master-token", token);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

            for (ExcelAddress data : dataFromExcel) {
                requestLimitService.checkRateLimit();

                ResponseEntity<AddressResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AddressResponseDTO.class, data.getAddress());

                AddressResponseDTO addressResponse = response.getBody();
                if (addressResponse != null && addressResponse.getAddresses() != null) {
                    for (AddressDTO addressDTO : addressResponse.getAddresses()) {
                        // Сохраните детали и значения адреса в базе данных
                        AddressDetail addressDetail = new AddressDetail();
                        AddressDetailsDTO addressDetailsDTO = addressDTO.getAddress_details();
                        addressDetail.setPostal_code(addressDetailsDTO.getPostal_code());
                        addressDetail.setIfns_ul(addressDetailsDTO.getIfns_ul());
                        addressDetail.setIfns_fl(addressDetailsDTO.getIfns_fl());
                        addressDetailRepository.save(addressDetail);

                        AddressValue addressValue = new AddressValue();
                        addressValue.setObjectId(addressDTO.getObject_id());
                        addressValue.setObjectLevelId(addressDTO.getObject_level_id());
                        addressValue.setOperationTypeId(addressDTO.getOperation_type_id());
                        addressValue.setObjectGuid(addressDTO.getObject_guid());
                        addressValue.setFullName(addressDTO.getFull_name());
                        addressValue.setRegionCode(addressDTO.getRegion_code());
                        addressValueRepository.save(addressValue);
                    }
                }
            }

            return "redirect:/success";
        } catch (RateLimitExceededException e) {
            return "Превышено ограничение количества запросов";
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


        int rowNum = 1;
        for (AddressValue addressValue : addressValues) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(addressValue.getId());
            row.createCell(1).setCellValue(addressValue.getObjectGuid());
            row.createCell(2).setCellValue(addressValue.getOperationTypeId());
            row.createCell(3).setCellValue(addressValue.getRegionCode());
            row.createCell(4).setCellValue(addressValue.getFullName());
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




