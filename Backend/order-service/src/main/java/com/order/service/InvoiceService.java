
package com.order.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.order.model.Order;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

  public byte[] generate(Order order) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document();
    PdfWriter.getInstance(doc, out);
    doc.open();

    doc.add(new Paragraph("INVOICE"));
    doc.add(new Paragraph("Order ID: " + order.getId()));
    doc.add(new Paragraph("Customer: " + order.getUserEmail()));

    for (var i : order.getItems()) {
      doc.add(new Paragraph(i.getProductName() + " x " + i.getQuantity()));
    }

    doc.add(new Paragraph("Total: $" + order.getTotalAmount()));
    doc.close();
    return out.toByteArray();
  }
}
