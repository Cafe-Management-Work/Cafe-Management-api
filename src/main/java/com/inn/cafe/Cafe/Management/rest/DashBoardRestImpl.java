package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashBoardRestImpl {

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("/details")
    public ResponseEntity<Map<String,Object>> getCount(){
        return ResponseEntity.ok(dashBoardService.getCount());
    }
}
