package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ClientRepository;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.mapper.ClientMapper;
import com.convention.service.mapper.ConventionMapper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for scanning PDF documents and extracting convention data.
 */
@Service
public class PdfScanService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfScanService.class);

    private final ClientRepository clientRepository;
    private final ConventionMapper conventionMapper;
    private final ClientMapper clientMapper;

    public PdfScanService(ClientRepository clientRepository, ConventionMapper conventionMapper, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.conventionMapper = conventionMapper;
        this.clientMapper = clientMapper;
    }

    /**
     * Scan PDF and extract convention data.
     *
     * @param file the PDF file to scan
     * @return ConventionDTO with extracted data
     */
    public ConventionDTO scanPdfForConvention(MultipartFile file) {
        LOG.debug("Scanning PDF file: {}", file.getOriginalFilename());

        try {
            // Extract text from PDF
            String pdfText = extractTextFromPdf(file);
            LOG.debug("Extracted text: {}", pdfText);

            // Try to extract QR code data first
            ConventionDTO qrData = extractDataFromQRCode(file);
            if (qrData != null) {
                LOG.debug("QR code data found, using it as primary source");
                return qrData;
            }

            // If no QR code, extract from text
            return extractDataFromText(pdfText);
        } catch (Exception e) {
            LOG.error("Error scanning PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors du scan du PDF: " + e.getMessage());
        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PdfReader reader = null;
        try {
            reader = new PdfReader(file.getInputStream());
            StringBuilder text = new StringBuilder();
            int numberOfPages = reader.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {
                text.append(PdfTextExtractor.getTextFromPage(reader, i));
                text.append("\n");
            }

            return text.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private ConventionDTO extractDataFromQRCode(MultipartFile file) {
        PDDocument document = null;
        try {
            document = Loader.loadPDF(file.getBytes());
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);

                try {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                    Result result = new MultiFormatReader().decode(bitmap);

                    String qrContent = result.getText();
                    LOG.debug("QR Code content: {}", qrContent);

                    return parseQRCodeContent(qrContent);
                } catch (Exception e) {
                    // Continue to next page if QR code not found on this page
                }
            }
        } catch (Exception e) {
            LOG.debug("No QR code found in PDF: {}", e.getMessage());
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    LOG.warn("Error closing PDDocument: {}", e.getMessage());
                }
            }
        }

        return null;
    }

    private ConventionDTO parseQRCodeContent(String qrContent) {
        // Format attendu: "Convention-{numConvention}-{id}"
        Pattern pattern = Pattern.compile("Convention-(\\d+)-(\\d+)");
        Matcher matcher = pattern.matcher(qrContent);

        if (matcher.find()) {
            ConventionDTO dto = new ConventionDTO();
            dto.setNumConvention(Long.parseLong(matcher.group(1)));
            // L'ID sera utilisé pour récupérer les données complètes si nécessaire
            return dto;
        }

        return null;
    }

    private ConventionDTO extractDataFromText(String text) {
        ConventionDTO dto = new ConventionDTO();

        // Extract convention number
        Pattern numPattern = Pattern.compile("CONVENTION N° (\\d+)|Numéro de convention:\\s*(\\d+)");
        Matcher numMatcher = numPattern.matcher(text);
        if (numMatcher.find()) {
            String num = numMatcher.group(1) != null ? numMatcher.group(1) : numMatcher.group(2);
            dto.setNumConvention(Long.parseLong(num));
        }

        // Extract dates
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        };

        // Date de signature
        Pattern dateSignPattern = Pattern.compile("Date de signature:\\s*(\\d{2}[/-]\\d{2}[/-]\\d{4})");
        Matcher dateSignMatcher = dateSignPattern.matcher(text);
        if (dateSignMatcher.find()) {
            dto.setDateSignConv(parseDate(dateSignMatcher.group(1), formatters));
        }

        // Date de début
        Pattern dateDebutPattern = Pattern.compile("Date de début:\\s*(\\d{2}[/-]\\d{2}[/-]\\d{4})");
        Matcher dateDebutMatcher = dateDebutPattern.matcher(text);
        if (dateDebutMatcher.find()) {
            dto.setDateDebutConv(parseDate(dateDebutMatcher.group(1), formatters));
        }

        // Date d'échéance
        Pattern echeancePattern = Pattern.compile("Date d'échéance:\\s*(\\d{2}[/-]\\d{2}[/-]\\d{4})");
        Matcher echeanceMatcher = echeancePattern.matcher(text);
        if (echeanceMatcher.find()) {
            dto.setEcheanceConv(parseDate(echeanceMatcher.group(1), formatters));
        }

        // Extract redevance
        Pattern redevancePattern = Pattern.compile("Redevance:\\s*([\\d,\\.]+)\\s*MRU");
        Matcher redevanceMatcher = redevancePattern.matcher(text);
        if (redevanceMatcher.find()) {
            String amount = redevanceMatcher.group(1).replace(",", ".");
            dto.setRedevance(new BigDecimal(amount));
        }

        // Extract responsable
        Pattern responsablePattern = Pattern.compile("Responsable:\\s*([^\\n\\r]+)");
        Matcher responsableMatcher = responsablePattern.matcher(text);
        if (responsableMatcher.find()) {
            dto.setNomResponsable(responsableMatcher.group(1).trim());
        }

        // Extract statut
        Pattern statutPattern = Pattern.compile("Statut:\\s*(\\w+)");
        Matcher statutMatcher = statutPattern.matcher(text);
        if (statutMatcher.find()) {
            try {
                dto.setStatut(StatutConvention.valueOf(statutMatcher.group(1)));
            } catch (IllegalArgumentException e) {
                LOG.warn("Unknown status: {}", statutMatcher.group(1));
                dto.setStatut(StatutConvention.BROUILLON);
            }
        }

        // Extract client name
        Pattern clientPattern = Pattern.compile("Client:\\s*([^\\n\\r]+)");
        Matcher clientMatcher = clientPattern.matcher(text);
        if (clientMatcher.find()) {
            String clientName = clientMatcher.group(1).trim();
            // Try to find client by name
            clientRepository
                .findAll()
                .stream()
                .filter(client -> client.getNomClient() != null && client.getNomClient().toLowerCase().contains(clientName.toLowerCase()))
                .findFirst()
                .ifPresent(client -> dto.setClient(clientMapper.toDto(client)));
        }

        return dto;
    }

    private LocalDate parseDate(String dateStr, DateTimeFormatter[] formatters) {
        String normalizedDate = dateStr.replace("-", "/");

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(normalizedDate, formatter);
            } catch (Exception e) {
                // Try next formatter
            }
        }

        LOG.warn("Could not parse date: {}", dateStr);
        return null;
    }
}
