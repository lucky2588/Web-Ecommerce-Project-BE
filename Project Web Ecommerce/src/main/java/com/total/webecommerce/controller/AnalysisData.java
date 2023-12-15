package com.total.webecommerce.controller;

import com.total.webecommerce.resquest.OfAdmin.AnalystSaleResquest;
import com.total.webecommerce.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/AnalysisData")
public class AnalysisData {
    @Autowired
    private AnalysisService service;

    @GetMapping("getSales")
    public ResponseEntity<?> getSales(@RequestParam String type , @RequestParam Long time){
        return service.getTargetSales(type,time);
    }




}
