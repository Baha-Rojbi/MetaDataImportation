package com.example.exportationmetadata.Services;

import com.example.exportationmetadata.Entities.ColumnInfo;
import com.example.exportationmetadata.Entities.FileInfo;
import com.example.exportationmetadata.Entities.FileInfoDto;
import com.example.exportationmetadata.Repositories.ColumnInfoRepository;
import com.example.exportationmetadata.Repositories.FileInfoRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private ColumnInfoRepository columnInfoRepository;
    @Transactional
    public String processFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        Optional<FileInfo> existingFile = fileInfoRepository.findByFileName(file.getOriginalFilename());
        existingFile.ifPresent(fileInfo -> {
            columnInfoRepository.deleteByFileInfo(fileInfo);
            fileInfoRepository.delete(fileInfo);
        });
        if (fileName.endsWith(".csv")) {
            return processCSV(file);
        } else if (fileName.endsWith(".xlsx")) {
            return processExcel(file);
        } else {
            throw new IllegalArgumentException("Le format de fichier n'est pas supporté. Veuillez uploader un fichier .csv ou .xlsx");
        }
    }

    private String processCSV(MultipartFile file) throws IOException, CsvValidationException {
        try (InputStream inputStream = file.getInputStream();// reads the file as bytes
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //input stream reader bridge from byte to chars and buffer reader reads characters into text
             CSVReader csvReader = new CSVReader(bufferedReader)) {
            String[] headers = csvReader.readNext();
            if (headers == null) {
                throw new IllegalArgumentException("Le fichier CSV est vide ou mal formaté");
            }

            Map<String, String> columnTypes = new HashMap<>();
            String[] nextLine = csvReader.readNext(); // Prendre la première ligne de données pour déterminer les types
            if (nextLine != null) {
                for (int i = 0; i < headers.length; i++) {
                    if (nextLine.length > i) {
                        String value = nextLine[i];
                        String type = determineDataType(value);
                        columnTypes.put(headers[i], type);
                    }
                }
            }

            saveFileMetadata(file.getOriginalFilename(), columnTypes);
            return "Fichier CSV traité avec succès";
        }
    }


    private String processExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // the first sheet of the file
            Iterator<Row> rowIterator = sheet.iterator(); //an iterator over the rows of that sheet

            Row headerRow = rowIterator.hasNext() ? rowIterator.next() : null; // checks if first row is not empty otherwise give it null
            if (headerRow == null) {
                throw new IllegalArgumentException("Le fichier Excel est vide ou mal formaté");
            }

            Map<String, String> columnTypes = new HashMap<>(); // map (column head , data type)
            if (rowIterator.hasNext()) { // checks if there is another row that contains data
                Row dataRow = rowIterator.next(); // Prendre la première ligne de données pour déterminer les types
                for (Cell cell : dataRow) {
                    int columnIndex = cell.getColumnIndex();
                    String header = headerRow.getCell(columnIndex).getStringCellValue();
                    String type = determineDataTypeForCell(cell);
                    columnTypes.put(header, type);
                }
            }

            saveFileMetadata(file.getOriginalFilename(), columnTypes);
            return "Fichier Excel traité avec succès";
        }
    }

    private void saveFileMetadata(String fileName, Map<String, String> columnTypes) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setCreationDate(LocalDateTime.now());
        fileInfo = fileInfoRepository.save(fileInfo);

        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(entry.getKey());
            columnInfo.setColumnType(entry.getValue());
            columnInfo.setFileInfo(fileInfo); // Associer chaque ColumnInfo au FileInfo
            columnInfoRepository.save(columnInfo);
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
// delete
@Transactional
    public void deleteFileById(Long id) {
        fileInfoRepository.findById(id).ifPresent(fileInfo -> {
            columnInfoRepository.deleteByFileInfo(fileInfo);
            fileInfoRepository.delete(fileInfo);
        });
    }
    @Transactional
    public void deleteFileByName(String name) {
        fileInfoRepository.findByFileName(name).ifPresent(fileInfo -> {
            columnInfoRepository.deleteByFileInfo(fileInfo);
            fileInfoRepository.delete(fileInfo);
        });
    }
// affichage avec dto
public List<FileInfoDto> getAllFilesWithMetadata() {
    List<FileInfo> files = fileInfoRepository.findAll();
    return files.stream().map(this::convertToDto).collect(Collectors.toList());
}

    private FileInfoDto convertToDto(FileInfo fileInfo) {
        FileInfoDto dto = new FileInfoDto();
        dto.setFileName(fileInfo.getFileName());
        dto.setCreationDate(fileInfo.getCreationDate());
        List<FileInfoDto.ColumnInfoDto> columnInfos = fileInfo.getColumnInfos().stream()
                .map(columnInfo -> {
                    FileInfoDto.ColumnInfoDto columnInfoDto = new FileInfoDto.ColumnInfoDto();
                    columnInfoDto.setColumnName(columnInfo.getColumnName());
                    columnInfoDto.setColumnType(columnInfo.getColumnType());
                    return columnInfoDto;
                }).collect(Collectors.toList());
        dto.setColumnInfos(columnInfos);
        return dto;
    }
}
