package com.total.webecommerce.service;

import com.total.webecommerce.response.AnalystRes;
import com.total.webecommerce.respository.OrOrder.PaymentRepository;
import com.total.webecommerce.resquest.OfAdmin.AnalystSaleResquest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AnalysisService {
    @Autowired
    private PaymentRepository paymentRepository;

    public ResponseEntity<?> getTargetSales(String type, Long time) {
        Date date = new Date(time);
        log.info("Log TIME" +date.getTime());
        log.info("Log DAY" +date.getDay());

        LocalDate localDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
        if (type.equals("MONTH")) {
            return ResponseEntity.ok(paymentRepository.getSaleTargetForYEAR(localDate));
        } else if (type.equals("YEAR")){
            return ResponseEntity.ok(paymentRepository.getSaleTargetForMonth(localDate));
        } else {
//
            return ResponseEntity.ok(paymentRepository.getSaleTargetForDay(localDate));
        }
    };
}
