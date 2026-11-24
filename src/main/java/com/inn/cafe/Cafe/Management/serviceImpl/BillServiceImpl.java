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
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Value("${cafe.reports.dir:CafeReports}")
    private String reportsDir;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Path getPdfPath(String uuid) {
        return Path.of(reportsDir, uuid + ".pdf");
    }

    @Override
    public byte[] getPdf(BillGenerateRequest billGenerateRequest) throws IOException {
        log.info("Pdf Generation Report : billGenerateRequest {}", billGenerateRequest);
        if (!StringUtils.hasText(billGenerateRequest.getUuid())) {
            throw new PdfValidationException(ExceptionConstants.UUID_MISSING);
        }

        Path pdfPath = getPdfPath(billGenerateRequest.getUuid());
        if (Files.exists(pdfPath)) {
            return getByteArray(pdfPath);
        } else {
            billGenerateRequest.setIsGenerate(false);
            generateReport(billGenerateRequest);
            return getByteArray(pdfPath);
        }
    }

    @Override
    public String deleteBill(Integer id) {
        Optional<Bill> bill = billDao.findById(id);
        if (bill.isPresent()) {
            billDao.deleteById(id);
            return String.format(CafeConstants.BILL_DELETED, id);
        } else {
            return String.format(CafeConstants.BILL_NOT_FOUND, id);
        }
    }

    private byte[] getByteArray(Path pdfPath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(pdfPath)) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }

    @Override
    public String generateReport(BillGenerateRequest billGenerateRequest) {
        log.info("Inside BillGenerate Report Service");

        String fileName;

        if (Boolean.FALSE.equals(billGenerateRequest.getIsGenerate()) && StringUtils.hasText(billGenerateRequest.getUuid())) {
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
        Path fullPath = getPdfPath(fileName);

        try {
            if (fullPath.getParent() != null) {
                Files.createDirectories(fullPath.getParent());
            }

            PdfWriter writer = new PdfWriter(fullPath.toString());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Cafe Management System")
                    .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(20));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Name: " + request.getName()));
            document.add(new Paragraph("Contact Number: " + request.getContactNumber()));
            document.add(new Paragraph("Email: " + request.getEmail()));
            document.add(new Paragraph("Payment Method: " + request.getPaymentMethod()));
            document.add(new Paragraph("\n"));

            List<Map<String, Object>> items = parseProductDetailsJson(request.getProductDetails());

            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 25, 15, 15, 15}));
            table.setWidth(UnitValue.createPercentValue(100));

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

            document.add(new Paragraph("\nTotal : " + request.getTotal())
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Thank you for visiting. Please visit again!!")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            log.info("PDF generated successfully at: {}", fullPath);

        } catch (IOException e) {
            log.error("Error generating PDF at path: {}", fullPath, e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addTableHeader(Table table, String headerText, String hexColor) {
        Color color = new DeviceRgb(
                Integer.parseInt(hexColor.substring(1, 3), 16),
                Integer.parseInt(hexColor.substring(3, 5), 16),
                Integer.parseInt(hexColor.substring(5, 7), 16)
        );

        table.addCell(new Cell()
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
        log.info("Bill Generate Request :{}", billGenerateRequest);
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
        if (jwtFilter.isAdmin()) {
            billList = billDao.getAllBills();
        } else {
            billList = billDao.getBillByUsername(jwtFilter.getCurrentUser());
        }
        return billList;
    }
}