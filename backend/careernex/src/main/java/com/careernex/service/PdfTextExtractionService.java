
package com.careernex.service;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class PdfTextExtractionService {

    public String extractText(String filePath) throws IOException {

        File file = new File(filePath);

        try (PDDocument document = Loader.loadPDF(file)) {

            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            String extractedText = pdfTextStripper.getText(document);

            return extractedText;
        }
    }
}
