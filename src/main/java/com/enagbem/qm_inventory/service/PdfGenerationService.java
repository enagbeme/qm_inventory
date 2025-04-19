package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.dto.SalesReportDTO;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGenerationService {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);

    @Autowired
    private ProductRepository productRepository;

    public ByteArrayInputStream generateSalesReport(List<SalesReportDTO> salesData,
                                                    LocalDateTime startDate,
                                                    LocalDateTime endDate,
                                                    String totalRevenue,
                                                    Long orderCount) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add company name
            Paragraph companyName = new Paragraph("QM Inventory", TITLE_FONT);
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setSpacingAfter(10);
            document.add(companyName);

            // Add title
            Paragraph title = new Paragraph("Sales Report", HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add report period
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Paragraph period = new Paragraph(String.format("Period: %s to %s",
                    startDate.format(formatter),
                    endDate.format(formatter)), NORMAL_FONT);
            period.setSpacingAfter(10);
            document.add(period);

            // Add summary statistics
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Total Revenue: " + totalRevenue + "\n", HEADER_FONT));
            summary.add(new Chunk("Total Orders: " + orderCount, HEADER_FONT));
            summary.setSpacingAfter(20);
            document.add(summary);

            // Create sales table
            PdfPTable table = new PdfPTable(6); // 6 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            addTableHeader(table);

            // Add table data
            for (SalesReportDTO sale : salesData) {
                addTableRow(table, sale);
            }

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Product", "Category", "Quantity Sold", "Revenue", "Cost", "Profit"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new Color(240, 240, 240));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, SalesReportDTO sale) {
        table.addCell(createCell(sale.getProductName()));
        table.addCell(createCell(sale.getCategoryName()));
        table.addCell(createCell(String.valueOf(sale.getTotalQuantitySold())));
        table.addCell(createCell(String.format("$%.2f", sale.getTotalRevenue())));
        table.addCell(createCell(String.format("$%.2f", sale.getTotalCost())));
        table.addCell(createCell(String.format("$%.2f", sale.getTotalProfit())));
    }

    private PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, NORMAL_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private String determineStockStatus(Integer currentStock, Integer minStockLevel) {
        if (currentStock == null || minStockLevel == null) {
            return "Unknown";
        }
        if (currentStock <= 0) {
            return "Out of Stock";
        }
        if (currentStock <= minStockLevel) {
            return "Low Stock";
        }
        return "In Stock";
    }

    public ByteArrayInputStream generateInventoryReport() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add company name
            Paragraph companyName = new Paragraph("QM Inventory", TITLE_FONT);
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setSpacingAfter(10);
            document.add(companyName);

            // Add title
            Paragraph title = new Paragraph("Inventory Report", HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Create inventory table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            String[] headers = {"Product", "Category", "Current Stock", "Min Stock Level", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new Color(240, 240, 240));
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Add product data
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                table.addCell(new Phrase(product.getName(), NORMAL_FONT));
                table.addCell(new Phrase(product.getCategory() != null ? product.getCategory().getName() : "N/A", NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(product.getCurrentStock()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(product.getMinStockLevel()), NORMAL_FONT));
                String stockStatus = determineStockStatus(product.getCurrentStock(), product.getMinStockLevel());
                table.addCell(new Phrase(stockStatus, NORMAL_FONT));
            }

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateProductsReport() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

         try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add company name
            Paragraph companyName = new Paragraph("QM Inventory", TITLE_FONT);
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setSpacingAfter(10);
            document.add(companyName);

            // Add title
            Paragraph title = new Paragraph("Products Report", HEADER_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Create products table
            PdfPTable table = new PdfPTable(5); // 5 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            String[] headers = {"Name", "Category", "Price", "Quantity", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new Color(240, 240, 240));
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Add product data
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                table.addCell(new Phrase(product.getName(), NORMAL_FONT));
                table.addCell(new Phrase(product.getCategory() != null ? product.getCategory().getName() : "N/A", NORMAL_FONT));
                table.addCell(new Phrase(String.format("$%.2f", product.getPrice()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(product.getCurrentStock()), NORMAL_FONT));
                String stockStatus = determineStockStatus(product.getCurrentStock(), product.getMinStockLevel());
                table.addCell(new Phrase(stockStatus, NORMAL_FONT));
            }

            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    }

