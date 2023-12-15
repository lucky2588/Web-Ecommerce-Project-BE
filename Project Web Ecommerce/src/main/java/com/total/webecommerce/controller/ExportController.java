package com.total.webecommerce.controller;

import com.itextpdf.text.DocumentException;
import com.total.webecommerce.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("api/v1/export")
public class ExportController {
    @Autowired
    private ExportService service;
    @GetMapping("getPDF")
    public ResponseEntity<?> getExport() throws DocumentException {
          byte[] pdfFile = service.exportFile();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.setContentDispositionFormData("attachment","bill.pdf");
        return new ResponseEntity<>(pdfFile,header, HttpStatus.OK);
    }

    @GetMapping("getExcal")
    public ResponseEntity<?> getExcal() {
        File excalFile = service.exportExcal();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.setContentDispositionFormData("attachment",excalFile.getName());
        Resource re  = new FileSystemResource(excalFile);
        return new ResponseEntity<>(re,header, HttpStatus.OK);
    }
}
