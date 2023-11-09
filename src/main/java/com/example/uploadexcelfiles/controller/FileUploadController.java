package com.example.uploadexcelfiles.controller;

import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.repository.AddressValueRepository;
import com.example.uploadexcelfiles.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {
//    @Autowired
//    private ExcelFileService excelFileService;
//
//    @Autowired
//    private AddressValueRepository addressValueRepository;
//
//    @GetMapping("/upload")
//    public String showUploadPage() {
//        return "upload";
//    }
//
//    @PostMapping("/upload")
//
//    public String uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            excelFileService.uploadAndParseExcelFile(file);
//            return "redirect:/success";
//        } catch (IOException e) {
//            return "Error uploading and parsing the file: " + e.getMessage();
//        }
//    }
//
//
//    @GetMapping("/success")
//    public String showSuccessPage() {
//        return "result";
//    }
}
