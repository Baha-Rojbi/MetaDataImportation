package tn.esprit.metadata.Services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.metadata.Entities.DataTable;
import tn.esprit.metadata.Entities.FileInfoDto;
import tn.esprit.metadata.Entities.Schema;
import tn.esprit.metadata.Repositories.SchemaRepository;
import tn.esprit.metadata.Repositories.TableRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileProcessingService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private SchemaRepository schemaRepository;
    @Transactional
    public String processFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        Optional<DataTable> existingFile = tableRepository.findByName(file.getOriginalFilename());
        existingFile.ifPresent(fileInfo -> {
            schemaRepository.deleteByParentDataTable(fileInfo);
            tableRepository.delete(fileInfo);
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
        DataTable fileInfo = new DataTable();
        fileInfo.setName(fileName);
        fileInfo.setCreationDate(LocalDateTime.now());
        fileInfo.setSize(0.0);
        fileInfo.setDescription("rien");
        fileInfo.setCreator("baha");

        fileInfo = tableRepository.save(fileInfo);

        for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
            Schema columnInfo = new Schema();
            columnInfo.setName(entry.getKey());
            columnInfo.setType(entry.getValue());
            columnInfo.setDescription("rien");
            columnInfo.setParentDataTable(fileInfo); // Associer chaque ColumnInfo au FileInfo
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
    // delete
    @Transactional
    public void deleteFileById(Long id) {
        tableRepository.findById(id).ifPresent(fileInfo -> {
            schemaRepository.deleteByParentDataTable(fileInfo);
            tableRepository.delete(fileInfo);
        });
    }
    @Transactional
    public void deleteFileByName(String name) {
        tableRepository.findByName(name).ifPresent(fileInfo -> {
            schemaRepository.deleteByParentDataTable(fileInfo);
            tableRepository.delete(fileInfo);
        });
    }

    // affichage avec dto
    public List<FileInfoDto> getAllFilesWithMetadata() {
        List<DataTable> files = tableRepository.findAll();
        return files.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private FileInfoDto convertToDto(DataTable fileInfo) {
        FileInfoDto dto = new FileInfoDto();
        dto.setIdTable(fileInfo.getIdTable());
        dto.setName(fileInfo.getName());
        dto.setCreationDate(fileInfo.getCreationDate());
        List<FileInfoDto.ColumnInfoDto> columnInfos = fileInfo.getSchemas().stream()
                .map(columnInfo -> {
                    FileInfoDto.ColumnInfoDto columnInfoDto = new FileInfoDto.ColumnInfoDto();
                    columnInfoDto.setName(columnInfo.getName());
                    columnInfoDto.setType(columnInfo.getType());
                    return columnInfoDto;
                }).collect(Collectors.toList());
        dto.setColumnInfos(columnInfos);
        return dto;
    }
}
