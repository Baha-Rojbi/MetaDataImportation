package com.example.metadataimportation.Services;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import com.example.metadataimportation.Repositories.SchemaRepository;
import com.example.metadataimportation.Repositories.TableRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Service
public class FileProcessService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private SchemaRepository schemaRepository;
    @Transactional
    public String processFile(MultipartFile file, String description) throws Exception {
        String fileName = file.getOriginalFilename();
        Optional<DataTable> existingFile = tableRepository.findByName(file.getOriginalFilename());
        existingFile.ifPresent(fileInfo -> {
            schemaRepository.deleteByParentDataTable(fileInfo);
            tableRepository.delete(fileInfo);
        });
        if (fileName.endsWith(".csv")) {
            return processCSV(file, description);
        } else if (fileName.endsWith(".xlsx")) {
            return processExcel(file, description);
        } else if (fileName.endsWith(".docx")) {
            return processWord(file, description);
        } else if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
            return processPDF(file, description);
        }
        else {
            throw new IllegalArgumentException("File format not supported. Please upload a .csv, .xlsx, or .docx file.");
        }
    }
    private String processPDF(MultipartFile file, String description) throws IOException {
        DataTable dataTable = new DataTable();
        String fileName=file.getOriginalFilename();
        String nameWithoutExtension = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        dataTable.setName(nameWithoutExtension);
        dataTable.setDescription(description);
        dataTable.setFileType("PDF");
        dataTable.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        dataTable.setSize((double) file.getSize());
        dataTable.setSource(file.getOriginalFilename());
        dataTable.setCreator("System");
        dataTable = tableRepository.save(dataTable);
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\n");

            // Start from line 1 to skip the first line (index 0)
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                // Assuming each line is a key-value pair like "Key : Value"
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    Schema schema = new Schema();
                    schema.setName(key);
                    schema.setType(determineDataTypeForPdf(value)); // You need to implement this method
                    schema.setDescription("Information about " + key);
                    schema.setParentDataTable(dataTable);
                    // Save schema...
                    schemaRepository.save(schema);
                }
            }
        }
        return "PDF file processed successfully";
    }

    private String determineDataTypeForPdf(String value) {
        // Implement logic to determine data type (e.g., String, Integer, Date)
        // This is a simplified example
        try {
            Integer.parseInt(value);
            return "Integer";
        } catch (NumberFormatException e) {
            // More checks (for Double, Date, etc.) can be added here
            return "String";
        }
    }


    private String processCSV(MultipartFile file, String description) throws IOException, CsvValidationException {
        try (InputStream inputStream = file.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReader(bufferedReader)) {
            String[] headers = csvReader.readNext();
            if (headers == null) {
                throw new IllegalArgumentException("The CSV file is empty or improperly formatted.");
            }

            Map<String, String> columnTypes = new HashMap<>();
            String[] nextLine = csvReader.readNext(); // Take the first data line to determine types
            if (nextLine != null) {
                for (int i = 0; i < headers.length; i++) {
                    if (nextLine.length > i) {
                        String value = nextLine[i];
                        String type = determineDataType(value);
                        columnTypes.put(headers[i], type);
                    }
                }
            }

            saveFileMetadata(file.getOriginalFilename(), columnTypes, description, file.getSize(), "CSV");
            return "CSV file processed successfully";
        }
    }



    private String processExcel(MultipartFile file, String description) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // the first sheet of the file
            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.hasNext() ? rowIterator.next() : null;
            if (headerRow == null) {
                throw new IllegalArgumentException("The Excel file is empty or improperly formatted.");
            }

            Map<String, String> columnTypes = new HashMap<>();
            if (rowIterator.hasNext()) {
                Row dataRow = rowIterator.next(); // Take the first data line to determine types
                for (Cell cell : dataRow) {
                    int columnIndex = cell.getColumnIndex();
                    String header = headerRow.getCell(columnIndex).getStringCellValue();
                    String type = determineDataTypeForCell(cell);
                    columnTypes.put(header, type);
                }
            }

            saveFileMetadata(file.getOriginalFilename(), columnTypes, description, file.getSize(), "EXCEL");
            return "Excel file processed successfully";
        }
    }

    private String processWord(MultipartFile file, String description) throws IOException {
        try (InputStream is = file.getInputStream();
             XWPFDocument document = new XWPFDocument(is)) {
            DataTable dataTable = new DataTable();
            String fileName=file.getOriginalFilename();
            String nameWithoutExtension = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
            dataTable.setName(nameWithoutExtension);
            dataTable.setDescription(description);
            dataTable.setFileType("WORD");
            dataTable.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            dataTable.setSize((double) file.getSize());
            dataTable.setSource(file.getOriginalFilename());
            dataTable.setCreator("System");
            // Other DataTable setup...
            dataTable = tableRepository.save(dataTable);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                String[] parts = text.split(":"); // Assuming a simple format: Name:Type:Description
                if (parts.length >= 3) {
                    Schema schema = new Schema();
                    schema.setName(parts[0]);
                    schema.setType(parts[1]);
                    schema.setDescription(parts[2]);
                    schema.setParentDataTable(dataTable);
                    // Other Schema setup...
                    schemaRepository.save(schema);
                }
            }
            return "Word file processed successfully";
        }
    }

    private void saveFileMetadata(String fileName, Map<String, String> columnTypes, String description, long fileSize, String fileType) {
        DataTable fileInfo = new DataTable();
        String nameWithoutExtension = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
        fileInfo.setName(nameWithoutExtension); // Set name without extension
        fileInfo.setSource(fileName); // Set source to original file name
        fileInfo.setFileType(fileType);
        fileInfo.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)); // Truncate seconds
        fileInfo.setModificationDate(null);
        fileInfo.setSize((double) fileSize / 1024); // Set size in KB
        fileInfo.setDescription(description);
        fileInfo.setCreator("System");

        fileInfo = tableRepository.save(fileInfo);

        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
            Schema columnInfo = new Schema();
            columnInfo.setName(entry.getKey());
            columnInfo.setType(entry.getValue());
            columnInfo.setDescription("Column of " + entry.getKey());
            columnInfo.setParentDataTable(fileInfo);
            schemaRepository.save(columnInfo);
        }
    }




    // type do donnee dans excel
    private String determineDataTypeForCell(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return "Date";
                } else {
                    return Double.class.getSimpleName();
                }
            case BOOLEAN:
                return Boolean.class.getSimpleName();
            case STRING:
                return String.class.getSimpleName();
            default:
                return "Unknown";
        }
    }
    // type de donnee dans csv
    private String determineDataType(String value) {
        try {
            Integer.parseInt(value);
            return "Integer";
        } catch (NumberFormatException e1) {
            try {
                Double.parseDouble(value);
                return "Double";
            } catch (NumberFormatException e2) {
                return "String";
            }
        }
    }
    //
}
