package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.dto.request.BillGenerateRequest;
import com.inn.cafe.Cafe.Management.pojo.Bill;
import com.inn.cafe.Cafe.Management.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillRestImpl {

    @Autowired
    private BillService billService;

    @PostMapping("/generateReport")
    public ResponseEntity<String> generateReport(@Valid @RequestBody BillGenerateRequest billGenerateRequest) throws FileNotFoundException {
        return ResponseEntity.ok(billService.generateReport(billGenerateRequest));
    }

    @GetMapping("/getBill")
    public ResponseEntity<List<Bill>> getBills(){
        return ResponseEntity.ok(billService.getAllBills());
    }

    @PostMapping("/getPdf")
    public ResponseEntity<byte[]> getPdf(@Valid @RequestBody BillGenerateRequest billGenerateRequest) throws IOException {
        return ResponseEntity.ok(billService.getPdf(billGenerateRequest));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(billService.deleteBill(id));
    }
}
