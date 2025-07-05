package com.mergefiles;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final String INPUT_FOLDER = "files";
    private static final String OUTPUT_FILE = "merged_output.pdf";
    private static final List<String> SUPPORTED_IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
    private static final String PDF_EXTENSION = ".pdf";

    public static void main(String[] args) {
        try {
            System.out.println("Starting PDF and Image Merger...");
            
            // Get all files from the input folder
            List<File> files = getFilesFromFolder(INPUT_FOLDER);
            
            if (files.isEmpty()) {
                System.out.println("No files found in the " + INPUT_FOLDER + " folder.");
                return;
            }

            // Sort files by name for consistent ordering
            files.sort((f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            
            System.out.println("Found " + files.size() + " files to process:");
            files.forEach(file -> System.out.println("  - " + file.getName()));

            // Merge files into a single PDF
            mergeFilesToPdf(files, OUTPUT_FILE);
            
            System.out.println("Successfully created merged PDF: " + OUTPUT_FILE);
            
        } catch (Exception e) {
            System.err.println("Error during merging process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<File> getFilesFromFolder(String folderPath) throws IOException {
        List<File> files = new ArrayList<>();
        Path path = Paths.get(folderPath);
        
        if (!Files.exists(path)) {
            throw new IOException("Input folder does not exist: " + folderPath);
        }

        File folder = path.toFile();
        File[] fileList = folder.listFiles();
        
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(PDF_EXTENSION) || 
                        SUPPORTED_IMAGE_EXTENSIONS.stream().anyMatch(fileName::endsWith)) {
                        files.add(file);
                    }
                }
            }
        }
        
        return files;
    }

    private static void mergeFilesToPdf(List<File> files, String outputPath) throws IOException {
        PdfWriter writer = new PdfWriter(outputPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        for (File file : files) {
            String fileName = file.getName().toLowerCase();
            
            try {
                if (fileName.endsWith(PDF_EXTENSION)) {
                    // Handle PDF files
                    mergePdfFile(file, pdfDoc);
                } else if (SUPPORTED_IMAGE_EXTENSIONS.stream().anyMatch(fileName::endsWith)) {
                    // Handle image files
                    addImageToPdf(file, document);
                }
                
                System.out.println("Processed: " + file.getName());
                
            } catch (Exception e) {
                System.err.println("Error processing file " + file.getName() + ": " + e.getMessage());
                // Continue with other files even if one fails
            }
        }

        document.close();
        pdfDoc.close();
        writer.close();
    }

    private static void mergePdfFile(File pdfFile, PdfDocument targetPdf) throws IOException {
        PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
        PdfDocument sourcePdf = new PdfDocument(reader);
        
        int numberOfPages = sourcePdf.getNumberOfPages();
        
        for (int i = 1; i <= numberOfPages; i++) {
            targetPdf.addPage(sourcePdf.getPage(i).copyTo(targetPdf));
        }
        
        sourcePdf.close();
        reader.close();
    }

    private static void addImageToPdf(File imageFile, Document document) throws IOException {
        try {
            Image image = new Image(ImageDataFactory.create(imageFile.getAbsolutePath()));
            
            // Scale image to fit page width while maintaining aspect ratio
            float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth() - 50; // 25px margin on each side
            float pageHeight = document.getPdfDocument().getDefaultPageSize().getHeight() - 50;
            
            float imageWidth = image.getImageWidth();
            float imageHeight = image.getImageHeight();
            
            // Calculate scaling to fit the page
            float scaleX = pageWidth / imageWidth;
            float scaleY = pageHeight / imageHeight;
            float scale = Math.min(scaleX, scaleY);
            
            image.setWidth(imageWidth * scale);
            image.setHeight(imageHeight * scale);
            
            // Center the image on the page
            image.setHorizontalAlignment(com.itextpdf.layout.property.HorizontalAlignment.CENTER);
            
            document.add(image);
            
        } catch (Exception e) {
            throw new IOException("Failed to add image " + imageFile.getName() + ": " + e.getMessage());
        }
    }
} 