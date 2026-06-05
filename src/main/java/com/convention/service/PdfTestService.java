package com.convention.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for generating test PDF documents for scanning demonstration.
 */
@Service
public class PdfTestService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfTestService.class);

    /**
     * Generate a test PDF with sample convention data.
     *
     * @return PDF as byte array
     */
    public byte[] generateTestConventionPdf() {
        LOG.debug("Generating test PDF for scanning demonstration");

        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("CONVENTION N° 98765", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Convention details table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            addTableRow(table, "Date de signature:", "15/03/2024");
            addTableRow(table, "Date de début:", "01/04/2024");
            addTableRow(table, "Date d'échéance:", "31/03/2025");
            addTableRow(table, "Redevance:", "75000 MRU");
            addTableRow(table, "Responsable:", "Ahmed Mohamed");
            addTableRow(table, "Statut:", "ACTIVE");
            addTableRow(table, "Client:", "Société Test SARL");

            document.add(table);
            document.add(new Paragraph(" "));

            // Add some descriptive text
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph description = new Paragraph(
                "Cette convention de test a été générée automatiquement pour démontrer " +
                "les capacités de scan PDF du système. Toutes les informations ci-dessus " +
                "peuvent être extraites automatiquement lors du processus de scan.",
                normalFont
            );
            description.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(description);
            document.add(new Paragraph(" "));

            // QR Code
            try {
                String qrContent = "Convention-98765-999";
                byte[] qrCodeImage = generateQRCode(qrContent);
                Image qrImage = Image.getInstance(qrCodeImage);
                qrImage.scaleToFit(100, 100);
                qrImage.setAlignment(Element.ALIGN_CENTER);
                document.add(qrImage);

                // QR Code description
                Paragraph qrDescription = new Paragraph(
                    "QR Code: " + qrContent,
                    FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY)
                );
                qrDescription.setAlignment(Element.ALIGN_CENTER);
                document.add(qrDescription);
            } catch (Exception e) {
                LOG.error("Error generating QR code for test PDF", e);
            }

            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            LOG.error("Error generating test PDF", e);
            throw new RuntimeException("Error generating test PDF", e);
        }
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private byte[] generateQRCode(String content) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        return baos.toByteArray();
    }
}
