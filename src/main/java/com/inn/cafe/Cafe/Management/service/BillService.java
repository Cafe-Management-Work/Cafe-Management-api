package com.inn.cafe.Cafe.Management.service;


import com.inn.cafe.Cafe.Management.dto.request.BillGenerateRequest;
import com.inn.cafe.Cafe.Management.pojo.Bill;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface BillService {

    String generateReport(BillGenerateRequest billGenerateRequest) throws FileNotFoundException;

    List<Bill> getAllBills();

   public  byte[] getPdf(BillGenerateRequest billGenerateRequest) throws IOException;

   public String deleteBill(Integer id);
}
