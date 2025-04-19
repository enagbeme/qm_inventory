package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.dto.OrderDTO;
import com.enagbem.qm_inventory.dto.OrderItemDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Customer;
import com.enagbem.qm_inventory.model.Order;
import com.enagbem.qm_inventory.model.OrderItem;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.repository.CustomerRepository;
import com.enagbem.qm_inventory.repository.ProductRepository;
import com.enagbem.qm_inventory.util.QRCodeGenerator;

import com.google.zxing.WriterException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

@Service
public class InvoiceService {

    private final OrderService orderService;
    private final QRCodeGenerator qrCodeGenerator;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    public InvoiceService(OrderService orderService, QRCodeGenerator qrCodeGenerator,
                         CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderService = orderService;
        this.qrCodeGenerator = qrCodeGenerator;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public byte[] generateInvoice(Long orderId) throws DocumentException, IOException, WriterException {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        Order order = convertDTOToOrder(orderDTO);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        addInvoiceHeader(document, order);
        addCustomerInfo(document, order);
        addOrderDetails(document, order);
        addQRCode(document, order);
        addTotalSection(document, order);
        document.close();

        return outputStream.toByteArray();
    }

    public byte[] generateReceipt(Long orderId) throws DocumentException, IOException, WriterException {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        Order order = convertDTOToOrder(orderDTO);
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        addReceiptHeader(document, order);
        addOrderSummary(document, order);
        addQRCode(document, order);
        document.close();

        return outputStream.toByteArray();
    }

    private void addInvoiceHeader(Document document, Order order) throws DocumentException {
        Paragraph title = new Paragraph("QM INVENTORY || INVOICE", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Paragraph invoiceInfo = new Paragraph();
        invoiceInfo.add(new Chunk("Invoice No: " + order.getOrderId() + "\n", HEADER_FONT));
        invoiceInfo.add(new Chunk("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(order.getOrderDate()) + "\n", NORMAL_FONT));
        document.add(invoiceInfo);
    }

    private void addCustomerInfo(Document document, Order order) throws DocumentException {
        Paragraph customerInfo = new Paragraph();
        customerInfo.add(new Chunk("\nBill To:\n", HEADER_FONT));
        customerInfo.add(new Chunk(order.getCustomer().getName() + "\n", NORMAL_FONT));
        customerInfo.add(new Chunk(order.getShippingAddress() + "\n", NORMAL_FONT));
        customerInfo.add(new Chunk(order.getCustomer().getEmail() + "\n", NORMAL_FONT));
        customerInfo.add(new Chunk(order.getCustomer().getPhone() + "\n", NORMAL_FONT));
        document.add(customerInfo);
    }

    private void addOrderDetails(Document document, Order order) throws DocumentException {
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(5); // 5 columns
        table.setWidthPercentage(100);

        // Add table headers
        Stream.of("Item", "Description", "Quantity", "Unit Price", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    table.addCell(header);
                });

        // Add items
        for (OrderItem item : order.getItems()) {
            table.addCell(new Phrase(String.valueOf(item.getProduct().getProductId()), NORMAL_FONT));
            table.addCell(new Phrase(item.getProduct().getName(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("$%.2f", item.getUnitPrice()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("$%.2f", item.getSubtotal()), NORMAL_FONT));
        }

        document.add(table);
    }

    private void addTotalSection(Document document, Order order) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph totalSection = new Paragraph();
        totalSection.setAlignment(Element.ALIGN_RIGHT);
        totalSection.add(new Chunk(String.format("Total Amount: $%.2f\n", order.getTotalAmount()), HEADER_FONT));
        document.add(totalSection);
    }

    private void addReceiptHeader(Document document, Order order) throws DocumentException {
        Paragraph title = new Paragraph("QM INVENTORY || RECEIPT", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Paragraph receiptInfo = new Paragraph();
        receiptInfo.add(new Chunk("Receipt No: " + order.getOrderId() + "\n", HEADER_FONT));
        receiptInfo.add(new Chunk("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getOrderDate()) + "\n", NORMAL_FONT));
        receiptInfo.add(new Chunk("Customer: " + order.getCustomer().getName() + "\n", NORMAL_FONT));
        document.add(receiptInfo);
    }

    private void addOrderSummary(Document document, Order order) throws DocumentException {
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(4); // 4 columns for receipt
        table.setWidthPercentage(100);

        // Add table headers
        Stream.of("Item", "Qty", "Price", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    table.addCell(header);
                });

        // Add items
        for (OrderItem item : order.getItems()) {
            table.addCell(new Phrase(item.getProduct().getName(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("$%.2f", item.getUnitPrice()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("$%.2f", item.getSubtotal()), NORMAL_FONT));
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        // Add total
        Paragraph totalSection = new Paragraph();
        totalSection.setAlignment(Element.ALIGN_RIGHT);
        totalSection.add(new Chunk(String.format("Total: $%.2f\n", order.getTotalAmount()), HEADER_FONT));
        totalSection.add(new Chunk("Payment Method: " + order.getPaymentMethod() + "\n", NORMAL_FONT));
        document.add(totalSection);
    }

    private Order convertDTOToOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Order order = new Order();
        order.setOrderId(orderDTO.getOrderId());
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.valueOf(orderDTO.getStatus().toString()));
        order.setOrderDate(orderDTO.getOrderDate());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setNotes(orderDTO.getNotes());
        order.setTotalAmount(orderDTO.getTotalAmount());

        // Convert OrderItemDTOs to OrderItems
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            order.getItems().add(orderItem);
        }

        return order;
    }

    private void addQRCode(Document document, Order order) throws DocumentException, WriterException, IOException {
        // Generate QR code data
        String qrData = String.format("OrderID:%d,Date:%s,Amount:%.2f",
                order.getOrderId(),
                new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate()),
                order.getTotalAmount());

        // Generate QR code image
        byte[] qrCode = qrCodeGenerator.generateQRCode(qrData, 100, 100);

        // Add QR code to document
        Image qrImage = Image.getInstance(qrCode);
        qrImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrImage);
    }
}