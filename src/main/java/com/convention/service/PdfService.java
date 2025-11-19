package com.convention.service;

import com.convention.config.PdfStorageConfig;
import com.convention.domain.ConventionEntity;
import com.convention.repository.ConventionRepository;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for generating PDF documents.
 */
@Service
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

    private final ConventionRepository conventionRepository;
    private final PdfStorageConfig pdfStorageConfig;

    public PdfService(ConventionRepository conventionRepository, PdfStorageConfig pdfStorageConfig) {
        this.conventionRepository = conventionRepository;
        this.pdfStorageConfig = pdfStorageConfig;
    }

    /**
     * Generate PDF for convention with QR code.
     *
     * @param conventionId the convention ID
     * @return PDF as byte array
     */
    public byte[] generateConventionPdf(Long conventionId) {
        LOG.debug("Generating PDF for convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("CONVENTION N° " + convention.getNumConvention(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Convention details table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            addTableRow(table, "Numéro de convention:", convention.getNumConvention().toString());
            addTableRow(table, "Date de signature:", convention.getDateSignConv().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            addTableRow(table, "Date de début:", convention.getDateDebutConv().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            addTableRow(table, "Date d'échéance:", convention.getEcheanceConv().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            addTableRow(table, "Redevance:", convention.getRedevance().toString() + " MRU");
            addTableRow(table, "Responsable:", convention.getNomResponsable());
            addTableRow(table, "Statut:", convention.getStatut().toString());
            addTableRow(table, "Client:", convention.getClient().getNomClient());

            document.add(table);
            document.add(new Paragraph(" "));

            // QR Code
            try {
                String qrContent = "Convention-" + convention.getNumConvention() + "-" + convention.getId();
                byte[] qrCodeImage = generateQRCode(qrContent);
                Image qrImage = Image.getInstance(qrCodeImage);
                qrImage.scaleToFit(100, 100);
                qrImage.setAlignment(Element.ALIGN_CENTER);
                document.add(qrImage);
            } catch (Exception e) {
                LOG.error("Error generating QR code", e);
            }

            document.close();
            byte[] pdfBytes = baos.toByteArray();

            // Sauvegarder le PDF sur le serveur
            savePdfToFile(conventionId, pdfBytes);

            return pdfBytes;
        } catch (DocumentException e) {
            LOG.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
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

    private void savePdfToFile(Long conventionId, byte[] pdfBytes) {
        try {
            // Créer le répertoire s'il n'existe pas
            Path storageDir = Paths.get(pdfStorageConfig.getStoragePath());
            Files.createDirectories(storageDir);

            // Nom du fichier
            String fileName = "convention-" + conventionId + ".pdf";
            Path filePath = storageDir.resolve(fileName);

            // Sauvegarder le fichier
            try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                fos.write(pdfBytes);
            }

            LOG.info("PDF sauvegardé: {}", filePath.toString());
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarde du PDF: {}", e.getMessage());
        }
    }
}
