package com.inn.cafe.Cafe.Management.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.cafe.Cafe.Management.constants.CafeConstants;
import com.inn.cafe.Cafe.Management.constants.ExceptionConstants;
import com.inn.cafe.Cafe.Management.customExceptions.PdfValidationException;
import com.inn.cafe.Cafe.Management.dao.BillDao;
import com.inn.cafe.Cafe.Management.dto.request.BillGenerateRequest;
import com.inn.cafe.Cafe.Management.jwt.JwtFilter;
import com.inn.cafe.Cafe.Management.pojo.Bill;
import com.inn.cafe.Cafe.Management.service.BillService;
import com.inn.cafe.Cafe.Management.utils.CafeUtils;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb; // Import DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String REPORT_BASE_DIR = "C:" + FileSystems.getDefault().getSeparator() +
            "Users" + FileSystems.getDefault().getSeparator() +
            "91994" + FileSystems.getDefault().getSeparator() +
            "OneDrive" + FileSystems.getDefault().getSeparator() +
            "Desktop" + FileSystems.getDefault().getSeparator() +
            "Cafe Management" + FileSystems.getDefault().getSeparator() +
            "CafeReports" + FileSystems.getDefault().getSeparator();

    @Override
    public byte[] getPdf(BillGenerateRequest billGenerateRequest) throws IOException {
        log.info("Pdf Generation Report : billGenerateRequest {}",billGenerateRequest);
        byte[] byteArray = new byte[0];
        if(!StringUtils.hasText(billGenerateRequest.getUuid())){
            throw new PdfValidationException(ExceptionConstants.UUID_MISSING);
        }
        String filePath = REPORT_BASE_DIR +"\\"+billGenerateRequest.getUuid()+".pdf";
        if(CafeUtils.isFileEists(filePath)){
            byteArray = getByteArray(filePath);
            return byteArray;
        }
        else{
            billGenerateRequest.setIsGenerate(false);
            generateReport(billGenerateRequest);
            byteArray = getByteArray(filePath);
            return byteArray;
        }
    }

    @Override
    public String deleteBill(Integer id) {

        Optional<Bill> bill = billDao.findById(id);
        if(bill.isPresent()){
            billDao.deleteById(id);
            return String.format(CafeConstants.BILL_DELETED,id);
        }else{
            return String.format(CafeConstants.BILL_NOT_FOUND,id);
        }

    }

    private byte[] getByteArray(String filePath) throws IOException {
        File fileInitialization = new File(filePath);
        InputStream inputStream = new FileInputStream(fileInitialization);
        byte[] bytes = StreamUtils.copyToByteArray(inputStream);
        inputStream.close();
        return bytes;
    }

    @Override
    public String generateReport(BillGenerateRequest billGenerateRequest) {
        log.info("Inside BillGenerate Report Service");

        String fileName;

        if (!billGenerateRequest.getIsGenerate() && StringUtils.hasText(billGenerateRequest.getUuid())) {
            fileName = billGenerateRequest.getUuid();
        } else {
            fileName = CafeUtils.getUUID();
            billGenerateRequest.setUuid(fileName);
            insertBill(billGenerateRequest);
        }

        generatePdf(billGenerateRequest, fileName);

        return fileName;
    }


    private void generatePdf(BillGenerateRequest request, String fileName) {
        Path fullPath = Path.of(REPORT_BASE_DIR, fileName + ".pdf");
        String destination = fullPath.toString();

        try {
            PdfWriter writer = new PdfWriter(destination);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // ... (Title and Customer details paragraphs remain the same) ...
            document.add(new Paragraph("Cafe Management System")
                    .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(20));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Name: " + request.getName()));
            document.add(new Paragraph("Contact Number: " + request.getContactNumber()));
            document.add(new Paragraph("Email: " + request.getEmail()));
            document.add(new Paragraph("Payment Method: " + request.getPaymentMethod()));
            document.add(new Paragraph("\n"));


            // Product Table
            List<Map<String, Object>> items = parseProductDetailsJson(request.getProductDetails());

            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 25, 15, 15, 15}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Use the corrected helper method
            addTableHeader(table, "Name", "#FFC000");
            addTableHeader(table, "Category", "#FFC000");
            addTableHeader(table, "Quantity", "#FFC000");
            addTableHeader(table, "Price", "#FFC000");
            addTableHeader(table, "Sub Total", "#FFC000");

            for (Map<String, Object> item : items) {
                table.addCell(String.valueOf(item.get("name")));
                table.addCell(String.valueOf(item.get("category")));
                table.addCell(String.valueOf(item.get("quantity")));
                table.addCell(String.valueOf(item.get("price")));
                table.addCell(String.valueOf(item.get("total")));
            }

            document.add(table);

            // ... (Total and Thank You paragraphs remain the same) ...
            document.add(new Paragraph("\nTotal : " + request.getTotal())
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Thank you for visiting. Please visit again!!")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            log.info("PDF generated successfully at: {}", destination);

        } catch (FileNotFoundException e) {
            log.error("Error generating PDF: File path not found: {}", destination, e);
        }
    }

    // Helper method to create formatted table headers (FIXED)
    private void addTableHeader(Table table, String headerText, String hexColor) {
        // Use DeviceRgb directly with integer representation of RGB components
        Color color = new DeviceRgb(
                Integer.valueOf(hexColor.substring(1, 3), 16), // R
                Integer.valueOf(hexColor.substring(3, 5), 16), // G
                Integer.valueOf(hexColor.substring(5, 7), 16)  // B
        );

        table.addCell(new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(headerText).setBold())
                .setBackgroundColor(color)
                .setTextAlignment(TextAlignment.CENTER));
    }


    private List<Map<String, Object>> parseProductDetailsJson(String jsonString) {
        if (!StringUtils.hasText(jsonString)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.error("Error parsing product details JSON string: {}", jsonString, e);
            return Collections.emptyList();
        }
    }

    private void insertBill(BillGenerateRequest billGenerateRequest) {
        log.info("Bill Generate Request :{}",billGenerateRequest);
        Bill bill = new Bill();
        bill.setUuid(billGenerateRequest.getUuid());
        bill.setName(billGenerateRequest.getName());
        bill.setEmail(billGenerateRequest.getEmail());
        bill.setTotal(billGenerateRequest.getTotal());
        bill.setContactNumber(billGenerateRequest.getContactNumber());
        bill.setCreatedBy(jwtFilter.getCurrentUser());
        bill.setPaymentMethod(billGenerateRequest.getPaymentMethod());
        bill.setProductDetails(billGenerateRequest.getProductDetails());
        billDao.save(bill);
    }

    @Override
    public List<Bill> getAllBills() {
        List<Bill> billList = new ArrayList<>();
        if(jwtFilter.isAdmin()){
            billList = billDao.getAllBills();
        }
        else{

            billList = billDao.getBillByUsername(jwtFilter.getCurrentUser());
        }
        return billList;
    }


}
